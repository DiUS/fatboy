package au.com.dius.fatboy.factory.semantic.date;

import au.com.dius.fatboy.factory.config.FactoryHint;
import au.com.dius.fatboy.factory.semantic.SemanticFieldFactory;
import au.com.dius.fatboy.factory.semantic.SemanticFieldFactoryHint;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class DateTimeStringSemanticFieldFactory extends FactoryHint implements SemanticFieldFactory<String> {
    private SemanticFieldFactoryHint<String> fieldMatcher;

    public DateTimeStringSemanticFieldFactory() {
        this(ISODateTimeFormat.dateHourMinute());
    }

    public DateTimeStringSemanticFieldFactory(DateTimeFormatter dateTimeFormatter) {
        fieldMatcher = new SemanticFieldFactoryHint<>(String.class);
        fieldMatcher.addFieldMatcher(Pattern.compile("^dateTime$"), () -> dateTimeFormatter.print(new DateTime()));
        fieldMatcher.addFieldMatcher(Pattern.compile("^.*DateTime(\\p{Upper}.*|$)"), () -> dateTimeFormatter.print(new DateTime()));
    }

    public boolean supports(Field field) {
        return fieldMatcher.supports(field);
    }

    public String create(Field field) {
        return fieldMatcher.create(field);
    }
}
