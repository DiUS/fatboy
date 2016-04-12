package au.com.dius.fatboy.factory.primitives;

import au.com.dius.fatboy.factory.impl.AbstractClassFactory;

import java.lang.reflect.Field;

public class IntFactory extends AbstractClassFactory<Integer> {
    @Override
    public boolean supports(Class clazz) {
        return clazz == int.class || clazz == Integer.class;
    }

    @Override
    public Integer create(Field field) {
        return (int) (Math.random() * 10000);
    }

}
