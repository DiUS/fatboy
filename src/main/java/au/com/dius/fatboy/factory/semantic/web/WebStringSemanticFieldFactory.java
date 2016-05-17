package au.com.dius.fatboy.factory.semantic.web;

import au.com.dius.fatboy.FatBoy;
import au.com.dius.fatboy.factory.config.FieldLength;
import au.com.dius.fatboy.factory.semantic.SemanticFieldFactoryHint;

import java.util.regex.Pattern;

public class WebStringSemanticFieldFactory extends SemanticFieldFactoryHint<String> {
    public WebStringSemanticFieldFactory() {
        this(FieldLength.constant(4));
    }

    public WebStringSemanticFieldFactory(FieldLength fieldLength) {
        super(String.class);
        addFieldMatcher(Pattern.compile("^url$"), () -> FatBoy.FAKER.internet().url());
        addFieldMatcher(Pattern.compile("^uri$"), () -> FatBoy.FAKER.internet().url());
        addFieldMatcher(Pattern.compile("Url$"), () -> FatBoy.FAKER.internet().url());
        addFieldMatcher(Pattern.compile(".*\\p{Lower}Uri$"), () -> FatBoy.FAKER.internet().url());
        addFieldMatcher(Pattern.compile(".*\\p{Lower}Url$"), () -> FatBoy.FAKER.internet().url());
    }
}
