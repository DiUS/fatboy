package au.com.dius.fatboy.factory.collections;

import au.com.dius.fatboy.FatBoy;
import au.com.dius.fatboy.factory.config.FieldLength;
import au.com.dius.fatboy.factory.impl.AbstractGenericClassFactory;

import java.lang.reflect.*;

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
        Object array = Array.newInstance(rawType.getComponentType(), length);
        Type typeToCreate = getTypeToCreate(rawType, actualTypeArguments);

        for (int x = 0; x < length; x++) {
            ((Object[]) array)[x] = fatBoy.createGeneric(typeToCreate);
        }

        return array;
    }

    private Type getTypeToCreate(Class rawType, Type[] actualTypeArguments) {
        if (actualTypeArguments != null && actualTypeArguments[0] instanceof ParameterizedType) {
            return actualTypeArguments[0];
        } else if (actualTypeArguments != null && actualTypeArguments[0] instanceof GenericArrayType){
            return ((GenericArrayType)actualTypeArguments[0]).getGenericComponentType();
        } else {
            return rawType.getComponentType();
        }
    }

    @Override
    public Object create(Field field, Class<?> clazz) {
        return create((Class) field.getType(), null);
    }
}
