package au.com.dius.fatboy.factory.impl;

import au.com.dius.fatboy.factory.semantic.SemanticFieldFactory;

import java.lang.reflect.Field;
import java.util.function.Supplier;

public abstract class AbstractSemanticFactory<T> extends AbstractClassFactory<T> {

    public T create(Field field, Supplier<T> defaultFactory) {
        SemanticFieldFactory<T> fieldFactory = getFactoryFor(field);
        if (fieldFactory != null) {
            return fieldFactory.create(field);
        } else {
            return defaultFactory.get();
        }
    }

    public <P> SemanticFieldFactory<P> getFactoryFor(Field field) {
        return hints.values().stream()
                .filter(x -> x instanceof SemanticFieldFactory)
                .map(x -> (SemanticFieldFactory<P>) x)
                .filter(x -> x.supports(field))
                .findFirst()
                .orElse(null);
    }
}
