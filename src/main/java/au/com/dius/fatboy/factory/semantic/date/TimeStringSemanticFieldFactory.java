package au.com.dius.fatboy.factory.semantic.date;

import au.com.dius.fatboy.factory.semantic.SemanticFieldFactoryHint;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.regex.Pattern;

public class TimeStringSemanticFieldFactory extends SemanticFieldFactoryHint<String> {
    public TimeStringSemanticFieldFactory() {
        this(ISODateTimeFormat.time());
    }

    public TimeStringSemanticFieldFactory(DateTimeFormatter timeFormatter) {
        super(String.class);
        addFieldMatcher(Pattern.compile("^time$"), () -> timeFormatter.print(new DateTime()));
        addFieldMatcher(Pattern.compile("(?!.*Date).*Time(\\p{Upper}|$).*$"), () -> timeFormatter.print(new DateTime()));
    }
}
