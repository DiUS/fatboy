package au.com.dius.fatboy.factory.semantic.date;

import au.com.dius.fatboy.factory.semantic.SemanticFieldFactoryHint;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.regex.Pattern;

public class DateTimeStringSemanticFieldFactory extends SemanticFieldFactoryHint<String> {
    public DateTimeStringSemanticFieldFactory() {
        this(ISODateTimeFormat.dateHourMinute());
    }

    public DateTimeStringSemanticFieldFactory(DateTimeFormatter dateTimeFormatter) {
        super(String.class);
        addFieldMatcher(Pattern.compile("^dateTime$"), () -> dateTimeFormatter.print(new DateTime()));
        addFieldMatcher(Pattern.compile("^.*DateTime(\\p{Upper}.*|$)"), () -> dateTimeFormatter.print(new DateTime()));
    }
}
