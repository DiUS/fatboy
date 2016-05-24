package au.com.dius.fatboy.factory.semantic.id;

import au.com.dius.fatboy.FatBoy;
import au.com.dius.fatboy.factory.config.FieldLength;
import au.com.dius.fatboy.factory.semantic.SemanticFieldFactoryHint;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Pattern;

public class IdentifierStringSemanticFieldFactory extends SemanticFieldFactoryHint<String> {
    public IdentifierStringSemanticFieldFactory() {
        this(FieldLength.constant(4));
    }

    public IdentifierStringSemanticFieldFactory(FieldLength fieldLength) {
        super(String.class);
        addFieldMatcher(Pattern.compile("^id$"), () -> FatBoy.FAKER.numerify(StringUtils.repeat("#", fieldLength.getLength())));
        addFieldMatcher(Pattern.compile("^.*\\p{Lower}Id$"), () -> FatBoy.FAKER.numerify(StringUtils.repeat("#", fieldLength.getLength())));
    }
}
