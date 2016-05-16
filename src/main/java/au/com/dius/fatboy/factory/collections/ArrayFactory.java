package au.com.dius.fatboy.factory.collections;

import au.com.dius.fatboy.FatBoy;
import au.com.dius.fatboy.factory.config.FieldLength;
import au.com.dius.fatboy.factory.impl.AbstractGenericClassFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class ArrayFactory extends AbstractGenericClassFactory<Object> {

    private FatBoy fatBoy;

    public ArrayFactory(FatBoy fatBoy) {
        super(Object.class, FieldLength.constant(4));
        this.fatBoy = fatBoy;
    }

    @Override
    public boolean supports(Class clazz, Type genericType) {
        return clazz.isArray();
    }

    @Override
    public Object create(Class rawType, Type[] actualTypeArguments) {
        int length = getHint(FieldLength.class).getLength();

        Object o = Array.newInstance(rawType, length);

        for (int x = 0; x < length; x++) {
            ((Object[]) o)[x] = fatBoy.createGeneric(rawType);
        }

        return o;
    }

    @Override
    public Object create(Field field) {
        return create((Class) field.getType().getComponentType(), null);
    }
}
