package au.com.dius.fatboy.factory.semantic.name;

import au.com.dius.fatboy.FatBoy;
import au.com.dius.fatboy.factory.config.FieldLength;
import au.com.dius.fatboy.factory.semantic.SemanticFieldFactoryHint;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Pattern;

public class NameStringSemanticFieldFactory extends SemanticFieldFactoryHint<String> {
    public NameStringSemanticFieldFactory() {
        super(String.class);
        addFieldMatcher(Pattern.compile("^name$"), () -> FatBoy.FAKER.name().name());
        addFieldMatcher(Pattern.compile("^lastName$"), () -> FatBoy.FAKER.name().lastName());
        addFieldMatcher(Pattern.compile("^lastname$"), () -> FatBoy.FAKER.name().lastName());
        addFieldMatcher(Pattern.compile("^surname$"), () -> FatBoy.FAKER.name().lastName());
        addFieldMatcher(Pattern.compile("^surName$"), () -> FatBoy.FAKER.name().lastName());
        addFieldMatcher(Pattern.compile("^firstName$"), () -> FatBoy.FAKER.name().firstName());
        addFieldMatcher(Pattern.compile("^firstname"), () -> FatBoy.FAKER.name().firstName());

        addFieldMatcher(Pattern.compile(".*\\p{Lower}First[N|n]ame(\\p{Upper}.*)?$"), () -> FatBoy.FAKER.name().firstName());
        addFieldMatcher(Pattern.compile(".*\\p{Lower}Last[N|n]ame(\\p{Upper}.*)?$"), () -> FatBoy.FAKER.name().firstName());
    }
}
