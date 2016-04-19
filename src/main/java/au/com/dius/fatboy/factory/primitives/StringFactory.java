package au.com.dius.fatboy.factory.primitives;

import au.com.dius.fatboy.FatBoy;
import au.com.dius.fatboy.factory.config.FieldLength;
import au.com.dius.fatboy.factory.impl.AbstractSemanticFactory;

import java.lang.reflect.Field;

import static org.apache.commons.lang.StringUtils.repeat;

public class StringFactory extends AbstractSemanticFactory<String> {

    @Override
    public boolean supports(Class clazz) {
        return clazz == String.class;
    }

    @Override
    public String create(Field field) {
        int fieldLength = getHint(FieldLength.class).getLength();
        return super.create(field, () -> FatBoy.FAKER.letterify(repeat("?", fieldLength)));
    }
}
