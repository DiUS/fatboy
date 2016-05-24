package au.com.dius.fatboy.factory.semantic.date;

import au.com.dius.fatboy.utils.ReflectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.lang.reflect.Field;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class DateTimeStringSemanticFieldFactoryTest {

    @Test
    public void shouldMatchFieldsCorrectly() {
        DateTimeStringSemanticFieldFactory factory = new DateTimeStringSemanticFieldFactory();

        assertThat(factory.supports(field("date")), is(false));
        assertThat(factory.supports(field("dateTime")), is(true));
        assertThat(factory.supports(field("datetime")), is(true));
        assertThat(factory.supports(field("someDate")), is(false));
        assertThat(factory.supports(field("somedateTime")), is(false));
        assertThat(factory.supports(field("someDateTime")), is(true));
        assertThat(factory.supports(field("someDateTimeThing")), is(true));
        assertThat(factory.supports(field("universalDateTimeout")), is(false));
        assertThat(factory.supports(field("random")), is(false));
    }

    private Field field(String fieldName) {
        return ReflectionUtils.getField(FieldMatcherTest.class, fieldName);
    }

    private static class FieldMatcherTest {
        private String date;
        private String dateTime;
        private String datetime;
        private String someDate;
        private String somedateTime;
        private String someDateTime;
        private String someDateTimeThing;
        private String universalDateTimeout;
        private String random;
    }

}