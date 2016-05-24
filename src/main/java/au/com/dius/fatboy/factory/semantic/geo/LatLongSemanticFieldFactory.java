package au.com.dius.fatboy.factory.semantic.geo;

import au.com.dius.fatboy.FatBoy;
import au.com.dius.fatboy.factory.semantic.SemanticFieldFactoryHint;

import java.util.regex.Pattern;

public class LatLongSemanticFieldFactory extends SemanticFieldFactoryHint<String> {
    public LatLongSemanticFieldFactory() {
        super(String.class);
        addFieldMatcher(Pattern.compile("^latitude$"), () -> FatBoy.FAKER.address().latitude());
        addFieldMatcher(Pattern.compile("^lat"), () -> FatBoy.FAKER.address().latitude());
        addFieldMatcher(Pattern.compile("^longitude$"), () -> FatBoy.FAKER.address().longitude());
        addFieldMatcher(Pattern.compile("^lon"), () -> FatBoy.FAKER.address().longitude());
    }
}
