package au.com.dius.fatboy.factory.semantic.web;

import au.com.dius.fatboy.utils.ReflectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.lang.reflect.Field;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(BlockJUnit4ClassRunner.class)
public class WebStringSemanticFieldFactoryTest {

    @Test
    public void shouldMatchFieldsCorrectly() {
        WebStringSemanticFieldFactory factory = new WebStringSemanticFieldFactory();

        assertThat(factory.supports(field("uri")), is(true));
        assertThat(factory.supports(field("url")), is(true));
        assertThat(factory.supports(field("stuffuri")), is(false));
        assertThat(factory.supports(field("stuffUri")), is(true));
        assertThat(factory.supports(field("stuffUrl")), is(true));
        assertThat(factory.supports(field("stuffUrination")), is(false));
        assertThat(factory.supports(field("stuffUrli")), is(false));
    }

    private Field field(String fieldName) {
        return ReflectionUtils.getField(FieldMatcherTest.class, fieldName);
    }

    private static class FieldMatcherTest {
        String uri;
        String url;
        String stuffuri;
        String stuffUri;
        String stuffUrl;
        String stuffUrination;
        String stuffUrli;
    }
}