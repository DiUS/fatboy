package au.com.dius.fatboy.factory.primitives;

import au.com.dius.fatboy.factory.impl.AbstractClassFactory;
import au.com.dius.fatboy.utils.RandomUtils;

import java.lang.reflect.Field;

public class BooleanFactory extends AbstractClassFactory<Boolean> {

    @Override
    public boolean supports(Class clazz) {
        return clazz == boolean.class || clazz == Boolean.class;
    }

    @Override
    public Boolean create(Field field) {
        return RandomUtils.random(true, false);
    }

}
