package au.com.dius.fatboy.factory.impl;

import au.com.dius.fatboy.factory.GenericClassFactory;
import au.com.dius.fatboy.factory.config.FactoryHint;

import java.lang.reflect.Field;

public abstract class AbstractGenericClassFactory<T> extends AbstractClassFactory<T> implements GenericClassFactory<T> {

    private Class<T> clazz;

    public AbstractGenericClassFactory(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    public AbstractGenericClassFactory(Class<T> clazz, FactoryHint config) {
        super(config);
        this.clazz = clazz;
    }

    @Override
    public boolean supports(Field field) {
        return supports(field.getType(), field.getGenericType());
    }

    @Override
    public boolean supports(Class clazz) {
        return this.clazz == clazz;
    }
}
