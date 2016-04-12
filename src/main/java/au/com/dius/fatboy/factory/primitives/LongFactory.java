package au.com.dius.fatboy.factory.primitives;

import au.com.dius.fatboy.factory.impl.AbstractClassFactory;

import java.lang.reflect.Field;

public class LongFactory extends AbstractClassFactory<Long> {
    @Override
    public boolean supports(Class clazz) {
        return clazz == long.class || clazz == Long.class;
    }

    @Override
    public Long create(Field field) {
        return (long) (Math.random() * 10000);
    }

}
