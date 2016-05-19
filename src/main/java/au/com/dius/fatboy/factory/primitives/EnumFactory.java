package au.com.dius.fatboy.factory.primitives;

import au.com.dius.fatboy.factory.impl.AbstractClassFactory;
import au.com.dius.fatboy.utils.RandomUtils;

import java.lang.reflect.Field;

public class EnumFactory extends AbstractClassFactory {

    @Override
    public boolean supports(Class clazz) {
        return clazz.isEnum();
    }

    @Override
    public Object create(Field field, Class clazz) {
        return RandomUtils.random(clazz.getEnumConstants());
    }
}
