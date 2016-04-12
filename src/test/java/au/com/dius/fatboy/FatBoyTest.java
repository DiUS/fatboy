package au.com.dius.fatboy;

import au.com.dius.fatboy.factory.collections.CollectionFactory;
import au.com.dius.fatboy.factory.config.FieldCount;
import au.com.dius.fatboy.factory.impl.AbstractClassFactory;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.lang.reflect.Field;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNot.not;

@RunWith(BlockJUnit4ClassRunner.class)
public class FatBoyTest {

    private FatBoy fatBoy;

    @Before
    public void setup() {
        fatBoy = new FatBoy();
    }

    @Test
    public void shouldIgnoreClasses() {
        PrimitiveClass primitiveClass = fatBoy.addIgnoredClass(String.class).create(PrimitiveClass.class);

        assertThat(primitiveClass.four, is(nullValue()));
    }

    @Test
    public void addsClassConstant() {
        PrimitiveClass primitiveClass = fatBoy.addClassConstant("hello").create(PrimitiveClass.class);

        assertThat(primitiveClass.four, is("hello"));
    }

    @Test
    public void addsFieldConstant() throws Exception {
        PrimitiveClass primitiveClass = fatBoy
                .addFieldConstant(PrimitiveClass.class, "four", "hello")
                .create(PrimitiveClass.class);

        assertThat(primitiveClass.four, is("hello"));
    }

    @Test
    public void shouldPopulatePrimitives() {
        PrimitiveClass result = fatBoy.create(PrimitiveClass.class);

        assertThat(result.one, not(0));
        assertThat(result.two, not(0));
        assertThat(result.three, notNullValue());
        assertThat(result.four, notNullValue());
    }

    @Test
    public void allowsPluginOfProviderTypes() {
        ListClass result = fatBoy.addClassFactory(List.class, () -> Arrays.asList("hello")).create(ListClass.class);

        assertThat(result.strings, is(Lists.newArrayList("hello")));
    }

    @Test(expected = ClassInstantiationException.class)
    public void failsWhenNonGenericListsAreAttemptedToBeCreated() {
        fatBoy.create(ListClass.class);
    }

    @Test
    public void shouldPopulateGenericTypes() {
        GenericListClass result = fatBoy.create(GenericListClass.class);

        assertThat(result.strings, notNullValue());
        assertThat(result.strings.size(), not(0));
        assertThat(result.strings.get(0).length(), greaterThan(0));
    }

    @Test
    public void populatesCompositeClasses() {
        CompositeClass result = fatBoy.create(CompositeClass.class);

        assertThat(result.primitives, notNullValue());
        assertThat(result.primitives.size(), not(0));
        assertThat(result.primitives.get(0).one, not(0));
        assertThat(result.primitives.get(0).three, not(0));
        assertThat(result.primitives.get(0).four.length(), greaterThan(0));
    }

    @Test
    public void populatesMaps() {
        MapClass result = fatBoy.create(MapClass.class);

        assertThat(result.primitives.entrySet().size(), greaterThan(0));
    }

    @Test
    public void populatesMultilevelGenericMaps() {
        MapMapClass result = fatBoy.create(MapMapClass.class);

        assertThat(result.primitives.entrySet().size(), greaterThan(0));
    }

    @Test
    public void populatesListsOfMaps() {
        ListMapClass create = fatBoy.create(ListMapClass.class);

        assertThat(create.primitives.get(0).entrySet().size(), greaterThan(0));
    }

    @Test
    public void appliesFactoryConfigForLists() {
        GenericListClass create = new FatBoy()
                .setFactoryConfig(CollectionFactory.class, FieldCount.random(3, 5))
                .create(GenericListClass.class);

        assertThat(create.strings.size(), greaterThanOrEqualTo(3));
        assertThat(create.strings.size(), lessThanOrEqualTo(5));
    }

    @Test
    public void appliesFieldFactories() {
        CompositeClass create = fatBoy
                .addFieldFactory(PrimitiveClass.class, "four", () -> "Stuff")
                .create(CompositeClass.class);

        assertThat(create.primitives.get(0).four, is("Stuff"));
    }

    @Test
    public void appliesFieldFactoriesNumber2() throws Exception {
        CompositeClass create = fatBoy
                .addFieldFactory(PrimitiveClass.class.getDeclaredField("four"), () -> "Stuff")
                .create(CompositeClass.class);

        assertThat(create.primitives.get(0).four, is("Stuff"));
    }

    @Test
    public void appliesOverrides() {
        UUID uuid = UUID.randomUUID();

        ConstructorTestClass create = fatBoy.create(ConstructorTestClass.class, map("reference", uuid));

        assertThat(create.reference, is(uuid));
    }

    @Test
    public void appliesFatBoyProvidedFactories() {
        UUID override = UUID.randomUUID();
        UUID uuid = UUID.randomUUID();

        ConstructorTestClass create = fatBoy
                .addFatBoyProvidedFactory(ConstructorTestClass.class, (fatBoy) -> new ConstructorTestClass(uuid))
                .create(ConstructorTestClass.class, map("reference", override));

        assertThat(create.reference, is(uuid));
    }

    @Test
    public void overridesObjectsThatArentTheFirstValue() {
        UUID uuid = UUID.randomUUID();

        ConstructorTestClassCaseTwo create = fatBoy.create(ConstructorTestClassCaseTwo.class, map("reference", uuid));

        assertThat(create.reference, is(uuid));
    }

    @Test
    public void customClassProvidersCanBePluggedIn() {
        PrimitiveClass expected = new PrimitiveClass();

        PrimitiveClass primitiveClass = fatBoy.addClassFactory(new PrimitiveProvider(expected)).create(PrimitiveClass.class);

        assertThat(primitiveClass, is(expected));
    }

    @Test
    public void customGenericClassFactoriesCanBePluggedIn() {
        List<String> expected = Lists.newArrayList("foosauce");

        GenericListClass genericListClass = fatBoy.addGenericClassFactory(List.class, (a, b) -> expected)
                .create(GenericListClass.class);

        assertThat(genericListClass.strings, is(expected));
    }

    @Test(expected = IllegalArgumentException.class)
    public void explodesWhenGenericClassFactoryAddedForNonGenericClass() {
        fatBoy.addGenericClassFactory(PrimitiveClass.class, (a, b) -> null).create(PrimitiveClass.class);
    }

    private static class PrimitiveClass {
        int one;
        long two;
        boolean three;
        String four;
    }

    private static class GenericListClass {
        List<String> strings;
    }

    private static class ListClass {
        List strings;
    }

    private static class CompositeClass {
        List<PrimitiveClass> primitives;
    }

    private static class MapClass {
        Map<String, String> primitives;
    }

    private static class MapMapClass {
        Map<Map<String, String>, String> primitives;
    }

    private static class ListMapClass {
        List<Map<String, String>> primitives;
    }

    private static class ConstructorTestClass {
        private UUID reference;

        ConstructorTestClass(UUID reference) {
            this.reference = reference;
        }
    }

    private static class ConstructorTestClassCaseTwo {
        private PrimitiveClass first;
        private UUID reference;

        ConstructorTestClassCaseTwo(PrimitiveClass first, UUID reference) {
            this.first = first;
            this.reference = reference;
        }
    }

    private static class PrimitiveProvider extends AbstractClassFactory<PrimitiveClass> {
        PrimitiveClass toBeReturned;

        PrimitiveProvider(PrimitiveClass toBeReturned) {
            this.toBeReturned = toBeReturned;
        }

        @Override
        public boolean supports(Class clazz) {
            return clazz == PrimitiveClass.class;
        }

        @Override
        public PrimitiveClass create(Field field) {
            return toBeReturned;
        }
    }

    private <K, V> Map<K, V> map(K key, V value) {
        return new HashMap<K, V>() {{
            put(key, value);
        }};
    }
}
