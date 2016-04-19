package au.com.dius.fatboy.factory.collections;

import au.com.dius.fatboy.FatBoy;
import au.com.dius.fatboy.factory.config.FieldLength;
import au.com.dius.fatboy.factory.impl.AbstractGenericClassFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MapFactory extends AbstractGenericClassFactory<Map> {
    private FatBoy fatBoy;

    public MapFactory(FatBoy fatBoy) {
        super(Map.class, FieldLength.constant(1));
        this.fatBoy = fatBoy;
    }

    @Override
    public boolean supports(Class clazz, Type genericType) {
        return clazz == Map.class && genericType != null;
    }

    @Override
    public Map create(Field field) {
        Type genericType = field.getGenericType();
        ParameterizedType impl = (ParameterizedType) genericType;
        return create(field.getType(), impl.getActualTypeArguments());
    }

    @Override
    public Map create(Class rawType, final Type[] actualParameters) {
        final Map instance = new HashMap();

        int fieldCount = getHint(FieldLength.class).getLength();

        for (int x = 0; x < fieldCount; x++) {
            Object keyValue = fatBoy.createGeneric(actualParameters[0]);
            Object valueValue = fatBoy.createGeneric(actualParameters[1]);

            instance.put(keyValue, valueValue);
        }

        return instance;
    }
}
