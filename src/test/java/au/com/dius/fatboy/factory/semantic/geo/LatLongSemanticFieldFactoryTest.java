package au.com.dius.fatboy.factory.semantic.geo;

import au.com.dius.fatboy.utils.ReflectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.lang.reflect.Field;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(BlockJUnit4ClassRunner.class)
public class LatLongSemanticFieldFactoryTest {

    @Test
    public void shouldMatchFieldsCorrectly() {
        LatLongSemanticFieldFactory factory = new LatLongSemanticFieldFactory();

        assertThat(factory.supports(field("latitude")), is(true));
        assertThat(factory.supports(field("lat")), is(true));
        assertThat(factory.supports(field("longitude")), is(true));
        assertThat(factory.supports(field("lon")), is(true));
        assertThat(factory.supports(field("somelatitude")), is(false));
        assertThat(factory.supports(field("somelongitude")), is(false));
        assertThat(factory.supports(field("lone")), is(false));
        assertThat(factory.supports(field("late")), is(false));
    }

    private Field field(String fieldName) {
        return ReflectionUtils.getField(FieldMatcherTest.class, fieldName);
    }

    private static class FieldMatcherTest {
        private String latitude;
        private String lat;
        private String longitude;
        private String lon;
        private String somelatitude;
        private String somelongitude;
        private String lone;
        private String late;
    }

}