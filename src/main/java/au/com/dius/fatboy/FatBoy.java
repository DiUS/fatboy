package au.com.dius.fatboy;

import au.com.dius.fatboy.factory.ClassFactory;
import au.com.dius.fatboy.factory.GenericClassFactory;
import au.com.dius.fatboy.factory.GenericTypeFactory;
import au.com.dius.fatboy.factory.config.FactoryConfig;
import au.com.dius.fatboy.utils.ReflectionUtils;
import com.github.javafaker.Faker;
import com.google.common.collect.Maps;

import java.lang.ref.SoftReference;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static au.com.dius.fatboy.utils.LambdaUtils.unchecked;

public class FatBoy {
    public static final Faker FAKER = new Faker();

    private List<Class> ignored = new ArrayList<Class>() {{
        add(SoftReference.class);
    }};

    private final FactoryRepository factoryRepository;

    public FatBoy() {
        factoryRepository = new FactoryRepository(this);
    }

    public <T> FatBoy registerClassFactory(ClassFactory<T> factory) {
        factoryRepository.addFactory(factory);
        return this;
    }

    public <T> FatBoy registerClassFactory(Class<T> clazz, Supplier<T> supplier) {
        factoryRepository.addFactory(clazz, supplier);
        return this;
    }

    public <T> FatBoy addClassConstant(T value) {
        factoryRepository.addFactory((Class<T>) value.getClass(), () -> value);
        return this;
    }

    public <T> FatBoy registerGenericClassFactory(Class<T> clazz, GenericTypeFactory<T> factory) {
        factoryRepository.addFactory(clazz, factory);
        return this;
    }

    public <T> FatBoy addFieldConstant(Class clazz, String field, T value) {
        return addFieldConstant(ReflectionUtils.getField(clazz, field), value);
    }

    public <T> FatBoy addFieldConstant(Field field, T value) {
        return registerFieldFactory(field, () -> value);
    }

    public <T> FatBoy registerFieldFactory(Class clazz, String field, Supplier<T> factory) {
        return registerFieldFactory(ReflectionUtils.getField(clazz, field), factory);
    }

    public <T> FatBoy registerFieldFactory(Field field, Supplier<T> factory) {
        factoryRepository.addFactory(field, factory);
        return this;
    }

    public <T> FatBoy registerFatBoyProvidedFactory(Class<T> clazz, FatBoyProvidedFactory<T> factory) {
        factoryRepository.addFactory(clazz, () -> factory.create(this));
        return this;
    }

    public <T extends ClassFactory> ClassFactory findFactory(Class<T> factoryClass) {
        return factoryRepository.findFactory(factoryClass);
    }

    public FatBoy addIgnoredClass(Class clazz) {
        ignored.add(clazz);
        return this;
    }

    public <T extends ClassFactory> FatBoy setFactoryConfig(Class<T> factoryClass, FactoryConfig factoryConfig) {
        findFactory(factoryClass).setConfig(factoryConfig);
        return this;
    }

    public <T> T create(Class<T> clazz) {
        return create(clazz, Maps.newHashMap());
    }

    public <T> T create(Class<T> clazz, Map<String, Object> overrides) {

        ClassFactory<T> factory = factoryRepository.getFactoryForClass(clazz);

        if (factory != null) {
            return factory.create(null);
        }

        if (clazz.isPrimitive()) {
            throw new ClassInstantiationException("Primitive has no factory: [" + clazz + "]");
        }

        if (clazz.isInterface()) {
            throw new ClassInstantiationException("Interface has no factory: [" + clazz + "]");
        }

        try {
            Constructor<T> constructor = ReflectionUtils.getDefaultOrFirstConstructor(clazz);
            constructor.setAccessible(true);
            return createInstance(constructor, overrides);
        } catch (ClassInstantiationException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Object createGeneric(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            Class rawType = (Class) parameterizedType.getRawType();
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

            GenericClassFactory factory = factoryRepository.getFactoryForGenericType(rawType, parameterizedType);

            if (factory != null) {
                return factory.create(rawType, actualTypeArguments);
            } else {
                throw new ClassInstantiationException("No generic factory for class " + rawType);
            }
        } else if (type instanceof Class) {
            return create((Class) type);
        } else {
            throw new ClassInstantiationException("Unknown generic type: [" + type + "]");
        }
    }

    private <T> T createInstance(Constructor<T> constructor, Map<String, Object> overrides)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {

        List<Object> args = Arrays.asList(constructor.getGenericParameterTypes()).stream().map(x -> {
            if (x instanceof Class) {
                return create((Class)x);
            } else {
                return createGeneric(x);
            }
        }).collect(Collectors.toList());

        T instance = constructor.newInstance(args.toArray());

        return setFields(instance, overrides);
    }

    private <T> T setFields(final T instance, Map<String, Object> overrides) {
        ReflectionUtils.getAllDeclaredFields(instance.getClass()).stream()
                .filter(x -> !ignored.contains(x.getType()))
                .forEach(unchecked(field -> {
                    field.setAccessible(true);

                    if (overrides.containsKey(field.getName())) {
                        field.set(instance, overrides.get(field.getName()));
                        return;
                    }

                    ClassFactory<T> factory = factoryRepository.<T>getFactoryForField(field);

                    if (factory == null) {
                        field.set(instance, create(field.getType()));
                    } else {
                        field.set(instance, factory.create(field));
                    }
                }));

        return instance;
    }

    @FunctionalInterface
    public static interface FatBoyProvidedFactory<T> {
        T create(FatBoy fatBoy);
    }
}
