package au.com.dius.fatboy.factory.semantic.date;

import au.com.dius.fatboy.utils.ReflectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.lang.reflect.Field;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class DateStringSemanticFieldFactoryTest {

    @Test
    public void shouldMatchFieldsCorrectly() {
        DateStringSemanticFieldFactory factory = new DateStringSemanticFieldFactory();

        assertThat(factory.supports(field("date")), is(true));
        assertThat(factory.supports(field("Date")), is(true));
        assertThat(factory.supports(field("someDate")), is(true));
        assertThat(factory.supports(field("somedate")), is(false));
        assertThat(factory.supports(field("mandate")), is(false));
        assertThat(factory.supports(field("areYouDateable")), is(false));
        assertThat(factory.supports(field("somedatetime")), is(false));
        assertThat(factory.supports(field("someDateTime")), is(false));
        assertThat(factory.supports(field("random")), is(false));
    }

    private Field field(String fieldName) {
        return ReflectionUtils.getField(FieldMatcherTest.class, fieldName);
    }

    private static class FieldMatcherTest {
        private String date;
        private String Date;
        private String someDate;
        private String somedate;
        private String mandate;
        private String somedatetime;
        private String someDateTime;
        private String areYouDateable;
        private String random;
    }

}