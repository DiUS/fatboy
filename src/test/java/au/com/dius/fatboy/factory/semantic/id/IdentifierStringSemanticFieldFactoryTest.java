package au.com.dius.fatboy.factory.semantic.id;

import au.com.dius.fatboy.utils.ReflectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.lang.reflect.Field;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(BlockJUnit4ClassRunner.class)
public class IdentifierStringSemanticFieldFactoryTest {

    @Test
    public void shouldMatchFieldsCorrectly() {
        IdentifierStringSemanticFieldFactory factory = new IdentifierStringSemanticFieldFactory();

        assertThat(factory.supports(field("id")), is(true));
        assertThat(factory.supports(field("dateid")), is(false));
        assertThat(factory.supports(field("idiot")), is(false));
    }

    private Field field(String fieldName) {
        return ReflectionUtils.getField(FieldMatcherTest.class, fieldName);
    }

    private static class FieldMatcherTest {
        private String id;
        private String dateid;
        private String idiot;
    }
}