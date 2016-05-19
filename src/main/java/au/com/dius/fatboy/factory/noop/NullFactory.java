package au.com.dius.fatboy.factory.noop;

import au.com.dius.fatboy.factory.impl.AbstractGenericClassFactory;
import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

public class NullFactory extends AbstractGenericClassFactory<Object> {

    private List<Class<?>> classesToReturnNullFor;

    public NullFactory() {
        super(Object.class);
        classesToReturnNullFor = Lists.newArrayList();
    }

    public void addIgnoredClass(Class<?> clazz) {
        classesToReturnNullFor.add(clazz);
    }

    @Override
    public boolean supports(Class clazz) {
        return classesToReturnNullFor.contains(clazz);
    }

    @Override
    public boolean supports(Class clazz, Type genericType) {
        return supports(clazz);
    }

    @Override
    public Object create(Class rawType, Type[] actualTypeArguments) {
        return null;
    }

    @Override
    public Object create(Field field, Class<?> clazz) {
        return null;
    }
}
