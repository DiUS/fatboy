package au.com.dius.fatboy.factory.collections;

import au.com.dius.fatboy.ClassInstantiationException;
import au.com.dius.fatboy.FatBoy;
import au.com.dius.fatboy.factory.config.FieldLength;
import au.com.dius.fatboy.factory.impl.AbstractGenericClassFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class CollectionFactory extends AbstractGenericClassFactory<Collection> {
    private static final Map<Class, Class> classesMap = Collections.unmodifiableMap(new HashMap<Class, Class>() {{
        put(List.class, ArrayList.class);
        put(Set.class, HashSet.class);
    }});

    private FatBoy fatBoy;

    public CollectionFactory(FatBoy fatBoy) {
        super(Collection.class, FieldLength.constant(1));
        this.fatBoy = fatBoy;
    }

    @Override
    public boolean supports(Class clazz, Type genericType) {
        return Collection.class.isAssignableFrom(clazz) && genericType instanceof ParameterizedType;
    }

    @Override
    public Collection create(Field field) {
        Type genericType = field.getGenericType();
        ParameterizedType impl = (ParameterizedType) genericType;
        return create(field.getType(), impl.getActualTypeArguments());
    }

    @Override
    public Collection create(Class rawType, final Type[] actualParameters) {
        int count = getHint(FieldLength.class).getLength();
        try {
            final Collection instance = (Collection) classesMap.get(rawType).newInstance();

            for (int x = 0; x < count; x++) {
                instance.add(fatBoy.createGeneric(actualParameters[0]));
            }
            return instance;
        } catch (Exception e) {
            throw new ClassInstantiationException("Unable to create collection with generic type [" + rawType + "]");
        }
    }
}