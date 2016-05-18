package au.com.dius.fatboy;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;

@RunWith(BlockJUnit4ClassRunner.class)
public class FatBoyClassFactoryTest {

    private FatBoy fatBoy;

    @Before
    public void setup() {
        fatBoy = new FatBoy();
    }

    @Test
    public void shouldUseClassFactoriesForRootObject() {
        BasicType expected = new BasicType();
        BasicType actual = fatBoy.registerClassFactory(BasicType.class, () -> expected).create(BasicType.class);

        assertThat(actual, sameInstance(expected));
    }

    @Test
    public void shouldUseClassFactoriesForGenericArrays() {
        BasicType expected = new BasicType();
        GenericBasicTypeArray actual = fatBoy.registerClassFactory(BasicType.class, () -> expected).create(GenericBasicTypeArray.class);

        assertThat(actual.genericInstance.genericArray[0], sameInstance(expected));
    }

    @Test
    public void shouldUseClassFactoriesForLists() {
        BasicType expected = new BasicType();
        ListType actual = fatBoy.registerClassFactory(BasicType.class, () -> expected).create(ListType.class);

        for (BasicType basicType : actual.list) {
            assertThat(basicType, sameInstance(expected));
        }
    }

    @Test
    public void shouldUseClassFactoriesForMaps() {
        BasicType expected = new BasicType();
        MapType actual = fatBoy.registerClassFactory(BasicType.class, () -> expected).create(MapType.class);

        for (Map.Entry<BasicType, String> basicTypeStringEntry : actual.map.entrySet()) {
            assertThat(basicTypeStringEntry.getKey(), sameInstance(expected));
        }
    }

    @Test
    public void shouldUseClassFactoriesForNestedGenericCreation() {
        BasicType expected = new BasicType();
        BaseGenericListTypeExtender actual = fatBoy.registerClassFactory(BasicType.class, () -> expected).create(BaseGenericListTypeExtender.class);

        for (BasicType basicType : actual.genericList) {
            assertThat(basicType, sameInstance(expected));
        }
    }

    @Test
    public void shouldUseClassFactoriesForNestedGenericArrayCreation() {
        BasicType expected = new BasicType();
        BaseGenericArrayTypeExtender actual = fatBoy.registerClassFactory(BasicType.class, () -> expected).create(BaseGenericArrayTypeExtender.class);

        for (List<BasicType> basicTypes : actual.genericArray) {
            for (BasicType basicType : basicTypes) {
                assertThat(basicType, sameInstance(expected));
            }
        }
    }

    @Test
    public void shouldUseClassFactoriesForClassesInClassesExtendingGenericSuperclasses() {
        BaseGenericArrayType expected = new BaseGenericArrayType();
        BaseGenericListTypeExtenderWithInner actual = fatBoy.registerClassFactory(BaseGenericArrayType.class, () -> expected)
                .create(BaseGenericListTypeExtenderWithInner.class);

        assertThat(actual.field, sameInstance(expected));
    }

    private static class BasicType {
        public String type;
    }

    private static class ListType {
        public List<BasicType> list;
    }

    private static class MapType {
        public Map<BasicType, String> map;
    }

    private static class BaseGenericListTypeExtenderWithInner extends BaseGenericListType<BasicType> {
        public BaseGenericArrayType<String> field;
    }

    private static class BaseGenericListTypeExtender extends BaseGenericListType<BasicType> {

    }

    private static class BaseGenericArrayTypeExtender extends BaseGenericArrayType<List<BasicType>> {

    }

    private static class GenericBasicTypeArray {
        public BaseGenericArrayType<BasicType> genericInstance;
    }

    private static class GenericBasicTypeList {
        public BaseGenericListType<BasicType> genericInstance;
    }

    private static class BaseGenericArrayType<T> {
        public T[] genericArray;
    }

    private static class BaseGenericListType<T> {
        public List<T> genericList;
    }
}
