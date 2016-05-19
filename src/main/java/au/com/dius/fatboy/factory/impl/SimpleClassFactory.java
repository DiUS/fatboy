package au.com.dius.fatboy.factory.impl;

import au.com.dius.fatboy.factory.GenericTypeFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class SimpleClassFactory<T> extends AbstractGenericClassFactory<T> {
    private GenericTypeFactory<T> factory;

    public SimpleClassFactory(Class<T> clazz, GenericTypeFactory<T> factory) {
        super(clazz);
        this.factory = factory;
    }

    @Override
    public boolean supports(Class clazz, Type genericType) {
        return supports(clazz);
    }

    @Override
    public T create(Field field, Class<?> clazz) {
        if (field != null) {
            return create(field.getType(), new Type[]{field.getGenericType()});
        } else {
            return create(clazz, null);
        }
    }

    @Override
    public T create(Class rawType, Type[] actualTypeArguments) {
        return factory.create(rawType, actualTypeArguments);
    }
}
