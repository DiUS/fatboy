package au.com.dius.fatboy.config;

import au.com.dius.fatboy.FatBoy;
import au.com.dius.fatboy.factory.config.FieldLength;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(BlockJUnit4ClassRunner.class)
public class ConfigurerTest {

    @Before
    public void setup() {
        DateTimeUtils.setCurrentMillisFixed(10101010);
        Configurer.reset();
    }

    @After
    public void teardown() {
        DateTimeUtils.setCurrentMillisSystem();
    }

    @Test
    public void shouldHaveDefaultDateTimeStringConfiguration() {
        DateClass dateClass = new FatBoy().create(DateClass.class);

        assertThat(dateClass.someDateTime, is(ISODateTimeFormat.dateHourMinute().print(new DateTime())));
    }

    @Test
    public void shouldConfigureDateTimeStrings() {
        DateTimeFormatter formatter = ISODateTimeFormat.dateHour();
        Configurer.configure().dateTimeStrings(formatter);
        DateClass dateClass = new FatBoy().create(DateClass.class);

        assertThat(dateClass.someDateTime, is(formatter.print(new DateTime())));
    }

    @Test
    public void shouldHaveDefaultDateStringConfiguration() {
        DateClass dateClass = new FatBoy().create(DateClass.class);

        assertThat(dateClass.aDate, is(ISODateTimeFormat.date().print(new DateTime())));
    }

    @Test
    public void shouldConfigureDates() {
        DateTimeFormatter formatter = ISODateTimeFormat.weekyearWeekDay();
        Configurer.configure().dateStrings(formatter);
        DateClass dateClass = new FatBoy().create(DateClass.class);

        assertThat(dateClass.aDate, is(formatter.print(new DateTime())));
    }

    @Test
    public void shouldHaveDefaultTimeStringConfiguration() {
        DateClass dateClass = new FatBoy().create(DateClass.class);

        assertThat(dateClass.someTime, is(ISODateTimeFormat.time().print(new DateTime())));
    }

    @Test
    public void shouldConfigureTimes() {
        DateTimeFormatter formatter = ISODateTimeFormat.basicDateTimeNoMillis();
        Configurer.configure().timeStrings(formatter);
        DateClass dateClass = new FatBoy().create(DateClass.class);

        assertThat(dateClass.someTime, is(formatter.print(new DateTime())));
    }

    @Test
    public void shouldConfigureStringLengths() {
        Configurer.configure().strings(FieldLength.constant(125));
        String string = new FatBoy().create(String.class);

        assertThat(string.length(), is(125));
    }

    @Test
    public void shouldConfigureIntLength() {
        Configurer.configure().integers(FieldLength.constant(125));
        PrimitiveClass primitives = new FatBoy().create(PrimitiveClass.class);

        assertThat(primitives.integer, is(125));
    }

    @Test
    public void shouldConfigureLongLength() {
        Configurer.configure().longints(FieldLength.constant(125));
        PrimitiveClass primitives = new FatBoy().create(PrimitiveClass.class);

        assertThat(primitives.longint, is(125L));
    }

    @Test
    public void shouldConfigureIdentifierStrings() {
        Configurer.configure().identifierStrings(FieldLength.constant(10));
        IdentifierClass id = new FatBoy().create(IdentifierClass.class);

        assertThat(id.id.length(), is(10));
    }

    @Test
    public void dateTimeStringsShouldHaveNoOverlappingMatchers() {
        Configurer.configure().dateStrings(ISODateTimeFormat.basicDate());
        Configurer.configure().timeStrings(ISODateTimeFormat.basicDate());

        DateClass dateClass = new FatBoy().create(DateClass.class);
        assertThat(dateClass.someDateTime, is(ISODateTimeFormat.dateHourMinute().print(new DateTime())));
    }

    @Test
    public void dateStringsShouldHaveNoOverlappingMatchers() {
        Configurer.configure().dateTimeStrings(ISODateTimeFormat.basicDate());
        Configurer.configure().timeStrings(ISODateTimeFormat.basicDate());

        DateClass dateClass = new FatBoy().create(DateClass.class);
        assertThat(dateClass.aDate, is(ISODateTimeFormat.date().print(new DateTime())));
    }

    @Test
    public void timeStringsShouldHaveNoOverlappingMatchers() {
        Configurer.configure().dateTimeStrings(ISODateTimeFormat.basicDate());
        Configurer.configure().dateStrings(ISODateTimeFormat.basicDate());

        DateClass dateClass = new FatBoy().create(DateClass.class);
        assertThat(dateClass.someTime, is(ISODateTimeFormat.time().print(new DateTime())));
    }

    private static class DateClass {
        private String aDate;
        private String someDateTime;
        private String someTime;
    }

    private static class IdentifierClass {
        private String id;
    }

    private static class PrimitiveClass {
        int integer;
        long longint;
    }

}