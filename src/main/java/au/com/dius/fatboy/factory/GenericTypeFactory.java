package au.com.dius.fatboy.factory;

import java.lang.reflect.Type;

@FunctionalInterface
public interface GenericTypeFactory<T> {
    T create(Class rawType, Type[] actualTypeParams);
}
