package au.com.dius.fatboy.factory.primitives;

import au.com.dius.fatboy.FatBoy;
import au.com.dius.fatboy.factory.impl.AbstractClassFactory;

import java.lang.reflect.Field;

public class StringFactory extends AbstractClassFactory<String> {
    @Override
    public boolean supports(Class clazz) {
        return clazz == String.class;
    }

    @Override
    public String create(Field field) {
        return FatBoy.FAKER.letterify("????");
    }

}
