package au.com.dius.fatboy.factory.impl;

import au.com.dius.fatboy.factory.GenericTypeFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class SimpleGenericFieldFactory<T> extends AbstractGenericClassFactory<T> {
    private Field field;
    private GenericTypeFactory<T> factory;

    public SimpleGenericFieldFactory(Field field, GenericTypeFactory<T> factory) {
        super((Class<T>) field.getClass());
        this.field = field;
        this.factory = factory;
    }

    @Override
    public boolean supports(Class clazz) {
        return false;
    }

    @Override
    public boolean supports(Field field) {
        return this.field.getDeclaringClass() == field.getDeclaringClass() &&
                this.field.getName().equals(field.getName());
    }

    @Override
    public boolean supports(Class clazz, Type genericType) {
        return clazz == field.getType() && genericType != null;
    }

    @Override
    public T create(Field field, Class<?> clazz) {
        return create(field.getType(), new Type[]{field.getGenericType()});
    }

    @Override
    public T create(Class rawType, Type[] actualTypeArguments) {
        return factory.create(rawType, actualTypeArguments);
    }
}
