package au.com.dius.fatboy.factory.semantic.date;

import au.com.dius.fatboy.utils.ReflectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.lang.reflect.Field;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(BlockJUnit4ClassRunner.class)
public class TimeStringSemanticFieldFactoryTest {

    @Test
    public void shouldMatchFieldsCorrectly() {
        TimeStringSemanticFieldFactory factory = new TimeStringSemanticFieldFactory();

        assertThat(factory.supports(field("time")), is(true));
        assertThat(factory.supports(field("someTime")), is(true));
        assertThat(factory.supports(field("someTimeThing")), is(true));
        assertThat(factory.supports(field("someTimeout")), is(false));
        assertThat(factory.supports(field("aDateWithTime")), is(false));
        assertThat(factory.supports(field("random")), is(false));
    }

    private Field field(String fieldName) {
        return ReflectionUtils.getField(FieldMatcherTest.class, fieldName);
    }

    private static class FieldMatcherTest {
        private String time;
        private String someTime;
        private String someTimeThing;
        private String someTimeout;
        private String aDateWithTime;
        private String random;
    }

}