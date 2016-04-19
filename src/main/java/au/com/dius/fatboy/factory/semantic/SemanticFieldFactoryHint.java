package au.com.dius.fatboy.factory.semantic;

import au.com.dius.fatboy.factory.config.FactoryHint;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class SemanticFieldFactoryHint<T> extends FactoryHint implements SemanticFieldFactory<T> {
    private Map<Pattern, Supplier<T>> fieldPatterns = new HashMap<>();
    private Class<T> clazz;

    public SemanticFieldFactoryHint(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    public void addFieldMatcher(Pattern pattern, Supplier<T> supplier) {
        fieldPatterns.put(pattern, supplier);
    }

    public boolean supports(Field field) {
        return field != null && clazz.isAssignableFrom(field.getType()) && findMatcher(field) != null;
    }

    public T create(Field field) {
        Pattern matcher = findMatcher(field);
        return fieldPatterns.get(matcher).get();
    }

    private Pattern findMatcher(Field field) {
        return fieldPatterns.keySet().stream()
                .filter(x -> x.matcher(field.getName()).matches())
                .findFirst().orElse(null);
    }
}
