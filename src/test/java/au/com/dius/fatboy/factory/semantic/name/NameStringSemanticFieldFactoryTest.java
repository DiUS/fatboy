package au.com.dius.fatboy.factory.semantic.name;

import au.com.dius.fatboy.factory.semantic.id.IdentifierStringSemanticFieldFactory;
import au.com.dius.fatboy.factory.semantic.id.IdentifierStringSemanticFieldFactoryTest;
import au.com.dius.fatboy.utils.ReflectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.lang.reflect.Field;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(BlockJUnit4ClassRunner.class)
public class NameStringSemanticFieldFactoryTest {

    @Test
    public void shouldMatchFieldsCorrectly() {
        NameStringSemanticFieldFactory factory = new NameStringSemanticFieldFactory();

        assertThat(factory.supports(field("name")), is(true));
        assertThat(factory.supports(field("namesake")), is(false));
        assertThat(factory.supports(field("firstName")), is(true));
        assertThat(factory.supports(field("firstname")), is(true));
        assertThat(factory.supports(field("lastName")), is(true));
        assertThat(factory.supports(field("lastname")), is(true));
        assertThat(factory.supports(field("clientLastName")), is(true));
        assertThat(factory.supports(field("clientLastname")), is(true));
        assertThat(factory.supports(field("clientLastNamesake")), is(false));
        assertThat(factory.supports(field("clientFirstName")), is(true));
        assertThat(factory.supports(field("clientFirstname")), is(true));
        assertThat(factory.supports(field("clientFirstNamesake")), is(false));
        assertThat(factory.supports(field("surName")), is(true));
        assertThat(factory.supports(field("surname")), is(true));
    }

    private Field field(String fieldName) {
        return ReflectionUtils.getField(FieldMatcherTest.class, fieldName);
    }

    private static class FieldMatcherTest {
        private String name;
        private String namesake;
        private String firstName;
        private String lastName;
        private String clientLastName;
        private String clientLastname;
        private String clientLastNamesake;
        private String firstname;
        private String clientFirstName;
        private String clientFirstname;
        private String clientFirstNamesake;
        private String lastname;
        private String surName;
        private String surname;
    }

}