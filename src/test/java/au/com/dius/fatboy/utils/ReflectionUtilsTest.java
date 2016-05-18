package au.com.dius.fatboy.utils;

import com.google.common.reflect.TypeResolver;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(BlockJUnit4ClassRunner.class)
public class ReflectionUtilsTest {

    @Test
    public void shouldReturnRawType() throws Exception {
        assertTrue(ReflectionUtils.getRawType(field(StringTestStuff.class, "array")) == String[].class);
        assertTrue(ReflectionUtils.getRawType(field(StringTestStuff.class, "list")) == List.class);
        assertTrue(ReflectionUtils.getRawType(field(StringTestStuff.class, "map")) == Map.class);
    }

    @Test
    public void shouldReturnActualTypeArguments() {
        assertEquals(ReflectionUtils.getActualTypeArgs(field(ListTestStuff.class, "array"))[0], List[].class);
        assertEquals(ReflectionUtils.getActualTypeArgs(field(ListTestStuff.class, "list"))[0], List.class);
        assertEquals(ReflectionUtils.getActualTypeArgs(field(ListTestStuff.class, "map"))[0], List.class);

        assertEquals(ReflectionUtils.getActualTypeArgs(field(StringTestStuff.class, "array"))[0],  String[].class);
        assertEquals(ReflectionUtils.getActualTypeArgs(field(StringTestStuff.class, "list"))[0],  String.class);
        assertEquals(ReflectionUtils.getActualTypeArgs(field(StringTestStuff.class, "map"))[0],  String.class);

        assertEquals(ReflectionUtils.getActualTypeArgs(field(GenericTest.class, "array"))[0].toString(), "java.util.Map<java.lang.String, java.lang.String>[]");
        assertEquals(ReflectionUtils.getActualTypeArgs(field(GenericTest.class, "list"))[0].toString(),  "java.util.Map<java.lang.String, java.lang.String>");
        assertEquals(ReflectionUtils.getActualTypeArgs(field(GenericTest.class, "map"))[0].toString(), "java.util.Map<java.lang.String, java.lang.String>");
    }

    @Test
    public void shouldReturnTrueForClassesThatAreGeneric() {
        assertThat(ReflectionUtils.classIsGeneric(List.class), is(true));
        assertThat(ReflectionUtils.classIsGeneric(String.class), is(false));
        assertThat(ReflectionUtils.classIsGeneric(GenericTest.class), is(true));
    }

    @Test
    public <T> void shouldReturnTrueForClassesThatAreArrayTypes() {
        assertThat(ReflectionUtils.isArrayType(String[].class), is(true));
        assertThat(ReflectionUtils.isArrayType(String.class), is(false));
        assertThat(ReflectionUtils.isArrayType(field(GenericTest.class, "array")), is(true));
        assertThat(ReflectionUtils.isArrayType(field(GenericTest.class, "list")), is(false));
    }

    private Type field(Class<?> clazz, String name) {
        try {
            Field declaredField = TestStuff.class.getDeclaredField(name);
            return resolveType(declaredField, ((ParameterizedType)clazz.getGenericSuperclass()).getActualTypeArguments(), declaredField.getDeclaringClass().getTypeParameters());
        } catch (Exception e) {
            return null;
        }
    }

    private class GenericTest extends TestStuff<Map<String, String>> {
    }

    private class StringTestStuff extends TestStuff<String> {
    }

    private class ListTestStuff extends TestStuff<List> {
    }

    private class TestStuff<T> {
        T[] array;
        List<T> list;
        Map<T, String> map;
    }

    private Type resolveType(Field field, Type[] types, Type[] genericTypes) {
        TypeResolver typeResolver = new TypeResolver();
        for (int x = 0; x < types.length; x++) {
            typeResolver = typeResolver.where(genericTypes[x], types[x]);
        }
        return typeResolver.resolveType(field.getGenericType());
    }
}