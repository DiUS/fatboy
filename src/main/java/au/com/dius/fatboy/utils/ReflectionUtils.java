package au.com.dius.fatboy.utils;

import com.google.common.collect.Lists;

import java.lang.reflect.*;
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


    public static Field getField(Class clazz, String fieldName) {
        return unchecked(() -> clazz.getDeclaredField(fieldName));
    }

    public static boolean isArrayType(Type type) {
        return (type instanceof Class && ((Class) type).isArray()) || type instanceof GenericArrayType;
    }

    public static boolean classIsGeneric(Class<?> clazz) {
        return clazz.getGenericSuperclass() != Object.class;
    }

    public static Class getRawType(Type type) {
        if (type instanceof GenericArrayType) {
            GenericArrayType arrayType = (GenericArrayType) type;
            return Array.newInstance((Class) ((ParameterizedType) arrayType.getGenericComponentType()).getRawType(), 0).getClass();
        }
        if (type instanceof ParameterizedType) {
            return (Class) ((ParameterizedType) type).getRawType();
        }

        return (Class) type;
    }

    public static Type[] getActualTypeArgs(Type type) {
        if (type instanceof GenericArrayType) {
            return new Type[]{ type };
        }
        if (type instanceof ParameterizedType) {
            return ((ParameterizedType) type).getActualTypeArguments();
        }

        return new Type[]{type};
    }

    public static boolean typeIsFullyResolved(Field field) {
        Type genericType = field.getGenericType();
        return typeIsFullyResolved(genericType);
    }

    public static Type[] getClassTypeArguments(Class<?> rawType) {
        if(rawType.getGenericSuperclass() instanceof ParameterizedType) {
            ParameterizedType genericSuperclass = (ParameterizedType) rawType.getGenericSuperclass();
            return genericSuperclass.getActualTypeArguments();
        }

        return new Type[]{rawType.getGenericSuperclass()};
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

    private static boolean typeIsFullyResolved(Type type) {
        if (type instanceof GenericArrayType) {
            return typeIsFullyResolved(((GenericArrayType) type).getGenericComponentType());
        }
        if (type instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
            return Lists.newArrayList(actualTypeArguments).stream().allMatch(ReflectionUtils::typeIsFullyResolved);
        }

        return type instanceof Class;
    }
}
