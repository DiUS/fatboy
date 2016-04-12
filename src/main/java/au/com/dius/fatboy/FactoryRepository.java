package au.com.dius.fatboy;

import au.com.dius.fatboy.factory.ClassFactory;
import au.com.dius.fatboy.factory.GenericClassFactory;
import au.com.dius.fatboy.factory.GenericTypeFactory;
import au.com.dius.fatboy.factory.collections.CollectionFactory;
import au.com.dius.fatboy.factory.collections.MapFactory;
import au.com.dius.fatboy.factory.impl.SimpleClassFactory;
import au.com.dius.fatboy.factory.impl.SimpleFieldFactory;
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

    public FactoryRepository(FatBoy fatboy) {
        factories = Lists.newArrayList();
        factories.add(new StringFactory());
        factories.add(new IntFactory());
        factories.add(new BooleanFactory());
        factories.add(new LongFactory());

        factories.add(new EnumFactory());

        factories.add(new CollectionFactory(fatboy));
        factories.add(new MapFactory(fatboy));

        factories.add(new UUIDFactory());
        factories.add(new DateTimeFactory());
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
        if (clazz.getGenericInterfaces().length == 0) {
            throw new IllegalArgumentException("Addition of a generic class factory must only be used for generic classes");
        }
        addFactory(new SimpleClassFactory<>(clazz, factory));
    }

    public <T extends ClassFactory> ClassFactory findFactory(Class<T> clazz) {
        return factories.stream().filter(x -> x.getClass().isAssignableFrom(clazz)).findFirst().orElse(null);
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
        return (ClassFactory<T>) factories.stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }
}
