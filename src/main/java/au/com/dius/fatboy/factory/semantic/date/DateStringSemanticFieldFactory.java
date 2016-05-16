package au.com.dius.fatboy.factory.semantic.date;

import au.com.dius.fatboy.factory.semantic.SemanticFieldFactoryHint;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.regex.Pattern;

public class DateStringSemanticFieldFactory extends SemanticFieldFactoryHint<String> {
    public DateStringSemanticFieldFactory() {
        this(ISODateTimeFormat.date());
    }

    public DateStringSemanticFieldFactory(DateTimeFormatter dateTimeFormatter) {
        super(String.class);
        addFieldMatcher(Pattern.compile("(^date$)|(.*Date(?!(time|Time))(\\p{Upper}|$).*)"), () -> dateTimeFormatter.print(new DateTime()));
    }
}
