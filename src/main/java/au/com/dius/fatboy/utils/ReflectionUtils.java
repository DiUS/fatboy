package au.com.dius.fatboy.utils;

import com.google.common.collect.Lists;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;

import static au.com.dius.fatboy.utils.LambdaUtils.unchecked;

public class ReflectionUtils {
    public static <T> Constructor<T> getDefaultOrFirstConstructor(Class<T> clazz) {
        Constructor<T> defaultConstructor = ReflectionUtils.findDefaultConstructor(clazz);
        if (defaultConstructor == null) return ReflectionUtils.findFirstConstructor(clazz);
        return defaultConstructor;
    }

    public static List<Field> getAllDeclaredFields(Class clazz) {
        List<Field> declaredFields = getFieldsInSuperclasses(clazz);
        return excludeSyntheticAndStaticFields(declaredFields);
    }

    @SuppressWarnings("unchecked")
    private static <T> Constructor<T> findDefaultConstructor(Class<T> clazz) {
        return (Constructor<T>) Lists.newArrayList(clazz.getDeclaredConstructors()).stream()
                .filter(x -> x.getParameters().length == 0)
                .findFirst().orElse(null);
    }

    @SuppressWarnings("unchecked")
    private static <T> Constructor<T> findFirstConstructor(Class<T> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        return (Constructor<T>) (constructors.length == 0 ? null : constructors[0]);
    }

    private static List<Field> excludeSyntheticAndStaticFields(List<Field> declaredFields) {
        return declaredFields.stream()
                .filter(x -> !x.isSynthetic())
                .filter(x -> !Modifier.isStatic(x.getModifiers()))
                .collect(Collectors.toList());
    }

    private static List<Field> getFieldsInSuperclasses(Class clazz) {
        List<Field> declaredFields = Lists.newArrayList(clazz.getDeclaredFields());
        Class superclass = clazz.getSuperclass();

        while (superclass != null) {
            declaredFields.addAll(Lists.newArrayList(superclass.getDeclaredFields()));
            superclass = superclass.getSuperclass();
        }

        return declaredFields;
    }

    public static Field getField(Class clazz, String fieldName) {
        return unchecked(() -> clazz.getDeclaredField(fieldName));
    }
}
