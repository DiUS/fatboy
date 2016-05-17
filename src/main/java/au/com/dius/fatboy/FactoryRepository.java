package au.com.dius.fatboy;

import au.com.dius.fatboy.config.Configurer;
import au.com.dius.fatboy.factory.ClassFactory;
import au.com.dius.fatboy.factory.GenericClassFactory;
import au.com.dius.fatboy.factory.GenericTypeFactory;
import au.com.dius.fatboy.factory.collections.ArrayFactory;
import au.com.dius.fatboy.factory.collections.CollectionFactory;
import au.com.dius.fatboy.factory.collections.MapFactory;
import au.com.dius.fatboy.factory.impl.SimpleClassFactory;
import au.com.dius.fatboy.factory.impl.SimpleFieldFactory;
import au.com.dius.fatboy.factory.impl.SimpleGenericFieldFactory;
import au.com.dius.fatboy.factory.noop.NullFactory;
import au.com.dius.fatboy.factory.primitives.*;
import au.com.dius.fatboy.factory.user.DateTimeFactory;
import au.com.dius.fatboy.factory.user.UUIDFactory;
import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

class FactoryRepository {
    private final List<ClassFactory> factories;
    private NullFactory nullFactory;

    public FactoryRepository(FatBoy fatboy) {
        nullFactory = new NullFactory();
        factories = Lists.newArrayList();
        factories.add(new StringFactory());
        factories.add(new IntFactory());
        factories.add(new BooleanFactory());
        factories.add(new LongFactory());

        factories.add(new EnumFactory());

        factories.add(new ArrayFactory(fatboy));

        factories.add(new CollectionFactory(fatboy));
        factories.add(new MapFactory(fatboy));

        factories.add(new UUIDFactory());
        factories.add(new DateTimeFactory());

        applyHints();
    }

    private void applyHints() {
        factories.forEach(Configurer::applyHints);
    }

    public <T> void addFactory(ClassFactory<T> classFactory) {
        factories.add(0, classFactory);
    }

    public <T> void addFactory(Class<T> clazz, Supplier<T> supplier) {
        addFactory(new SimpleClassFactory<>(clazz, (raw, actual) -> supplier.get()));
    }

    public <T> void addFactory(Field field, Supplier<T> factory) {
        addFactory(new SimpleFieldFactory<>(field, factory));
    }

    public <T> void addFactory(Class<T> clazz, GenericTypeFactory<T> factory) {
        if (clazz.getTypeParameters().length == 0) {
            throw new IllegalArgumentException("Addition of a generic class factory must only be used for generic classes");
        }
        addFactory(new SimpleClassFactory<>(clazz, factory));
    }

    public <T> void addFactory(Field field, GenericTypeFactory<T> factory) {
        if (field.getType().getTypeParameters().length == 0) {
            throw new IllegalArgumentException("Addition of a generic field factory must only be used for generic fields");
        }
        addFactory(new SimpleGenericFieldFactory<>(field, factory));
    }

    public void addIgnoredClass(Class<?> clazz) {
        nullFactory.addIgnoredClass(clazz);
    }

    public <T extends ClassFactory> ClassFactory findFactory(Class<T> clazz) {
        return getFactory(x -> x.getClass().isAssignableFrom(clazz));
    }

    public <T> ClassFactory<T> getFactoryForClass(Class<T> clazz) {
        return getFactory(x -> x.supports(clazz));
    }

    public <T> ClassFactory<T> getFactoryForField(Field field) {
        return getFactory(x -> x.supports(field));
    }

    public GenericClassFactory getFactoryForGenericType(Class clazz, Type genericType) {
        return (GenericClassFactory) getFactory(x -> {
            return x instanceof GenericClassFactory && ((GenericClassFactory) x).supports(clazz, genericType);
        });
    }

    @SuppressWarnings("unchecked")
    private <T> ClassFactory<T> getFactory(Predicate<ClassFactory> predicate) {
        if(predicate.test(nullFactory)) {
            return (ClassFactory<T>)nullFactory;
        }

        return (ClassFactory<T>) factories.stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }
}
