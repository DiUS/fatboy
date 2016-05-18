package au.com.dius.fatboy;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.hamcrest.core.Is.is;

@RunWith(BlockJUnit4ClassRunner.class)
public class FatBoyFieldFactoryTest {

    private FatBoy fatBoy;

    @Before
    public void setup() {
        fatBoy = new FatBoy();
    }

    @Test
    public void shouldUseFieldFactoriesForRootObject() {
        BasicType actual = fatBoy.registerFieldFactory(BasicType.class, "type", () -> "hello").create(BasicType.class);

        assertThat(actual.type, is("hello"));
    }

    @Test
    public void shouldUseFieldFactoriesForGenericArrays() {
        BaseGenericArrayType actual = fatBoy.registerFieldFactory(BaseGenericArrayType.class, "genericArray", () -> new String[]{"hello"}).create(BaseGenericArrayType.class);

        assertThat(actual.genericArray[0], is("hello"));
    }

    @Test
    public void shouldUseFieldFactoriesForLists() {
        List<BasicType> expected = Lists.newArrayList();
        ListType actual = fatBoy.registerFieldFactory(ListType.class, "list", () -> expected).create(ListType.class);

        assertThat(actual.list, sameInstance(expected));
    }

    @Test
    public void shouldUseFieldFactoriesForMaps() {
        HashMap<BasicType, String> expected = Maps.newHashMap();

        MapType actual = fatBoy.registerFieldFactory(MapType.class, "map", () -> expected).create(MapType.class);
        assertThat(actual.map, sameInstance(expected));
    }

    @Test
    public void shouldUseFieldFactoriesForGenericLists() {
        List<BasicType> expected = Lists.newArrayList();
        GenericBasicTypeList actual = fatBoy.registerFieldFactory(BaseGenericListType.class, "genericList", () -> expected)
                .create(GenericBasicTypeList.class);

        assertThat(actual.genericInstance.genericList, sameInstance(expected));
    }

    @Test
    public void shouldUseFieldFactoriesForNestedGenericCreation() {
        List<BasicType> expected = Lists.newArrayList();
        BaseGenericListTypeExtender actual = fatBoy.registerFieldFactory(BaseGenericListType.class, "genericList", () -> expected)
                .create(BaseGenericListTypeExtender.class);

        assertThat(actual.genericList, sameInstance(expected));
    }

    @Test
    public void shouldUseFieldFactoriesForNestedGenericArrayCreation() {
        List<BasicType>[] expected = new List[]{};
        BaseGenericArrayTypeExtender actual = fatBoy.registerFieldFactory(BaseGenericArrayType.class, "genericArray", () -> expected)
                .create(BaseGenericArrayTypeExtender.class);

        assertThat(actual.genericArray, sameInstance(expected));
    }

    @Test
    public void shouldUseFieldFactoriesForFieldsInClassesExtendingGenericSuperclasses() {
        BaseGenericArrayType<String> expected = new BaseGenericArrayType<>();
        BaseGenericListTypeExtenderWithInner actual = fatBoy.registerFieldFactory(BaseGenericListTypeExtenderWithInner.class, "field", () -> expected)
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
