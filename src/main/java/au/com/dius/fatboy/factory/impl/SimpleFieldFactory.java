package au.com.dius.fatboy.factory.impl;

import java.lang.reflect.Field;
import java.util.function.Supplier;

public class SimpleFieldFactory<T> extends AbstractClassFactory<T> {
    private Field field;
    private Supplier<T> factory;

    public SimpleFieldFactory(Field field, Supplier<T> factory) {
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
    public T create(Field field) {
        return factory.get();
    }
}
