package au.com.dius.fatboy.config;

import au.com.dius.fatboy.factory.ClassFactory;
import au.com.dius.fatboy.factory.collections.CollectionFactory;
import au.com.dius.fatboy.factory.config.FactoryHint;
import au.com.dius.fatboy.factory.config.FieldLength;
import au.com.dius.fatboy.factory.primitives.IntFactory;
import au.com.dius.fatboy.factory.primitives.LongFactory;
import au.com.dius.fatboy.factory.primitives.StringFactory;
import au.com.dius.fatboy.factory.semantic.date.DateStringSemanticFieldFactory;
import au.com.dius.fatboy.factory.semantic.date.DateTimeStringSemanticFieldFactory;
import au.com.dius.fatboy.factory.semantic.date.TimeStringSemanticFieldFactory;
import au.com.dius.fatboy.factory.semantic.id.IdentifierStringSemanticFieldFactory;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.joda.time.format.DateTimeFormatter;

import java.util.Collection;

public class Configurer {

    private static final Configurer INSTANCE = new Configurer();

    private final Multimap<Class<? extends ClassFactory>, FactoryHint> configuration;

    private Configurer() {
        configuration = defaultConfiguration();
    }

    public Configurer dateTimeStrings(DateTimeFormatter formatter) {
        replaceHint(StringFactory.class, new DateTimeStringSemanticFieldFactory(formatter));
        return this;
    }

    public Configurer dateStrings(DateTimeFormatter formatter) {
        replaceHint(StringFactory.class, new DateStringSemanticFieldFactory(formatter));
        return this;
    }

    public Configurer timeStrings(DateTimeFormatter formatter) {
        replaceHint(StringFactory.class, new TimeStringSemanticFieldFactory(formatter));
        return this;
    }

    public Configurer identifierStrings(FieldLength fieldLength) {
        replaceHint(StringFactory.class, new IdentifierStringSemanticFieldFactory(fieldLength));
        return this;
    }

    public Configurer collections(FieldLength fieldLength) {
        replaceHint(CollectionFactory.class, fieldLength);
        return this;
    }

    public Configurer strings(FieldLength fieldLength) {
        replaceHint(StringFactory.class, fieldLength);
        return this;
    }

    public Configurer integers(FieldLength fieldLength) {
        replaceHint(IntFactory.class, fieldLength);
        return this;
    }

    public Configurer longints(FieldLength fieldLength) {
        replaceHint(LongFactory.class, fieldLength);
        return this;
    }

    private <T extends ClassFactory> void replaceHint(Class<T> factoryClass, FactoryHint replacement) {
        Collection<FactoryHint> factoryHints = configuration.get(factoryClass);

        FactoryHint existingHint = factoryHints.stream()
                .filter(x -> x.getClass() == replacement.getClass())
                .findFirst().orElse(null);

        factoryHints.remove(existingHint);
        factoryHints.add(replacement);
    }

    private Multimap<Class<? extends ClassFactory>, FactoryHint> defaultConfiguration() {
        Multimap<Class<? extends ClassFactory>, FactoryHint> configuration = ArrayListMultimap.create();

        configuration.put(CollectionFactory.class, FieldLength.random(2, 5));
        configuration.put(StringFactory.class, FieldLength.random(10, 25));
        configuration.put(StringFactory.class, new DateTimeStringSemanticFieldFactory());
        configuration.put(StringFactory.class, new DateStringSemanticFieldFactory());
        configuration.put(StringFactory.class, new TimeStringSemanticFieldFactory());
        configuration.put(StringFactory.class, new IdentifierStringSemanticFieldFactory());
        configuration.put(IntFactory.class, FieldLength.random(0, 100000));
        configuration.put(LongFactory.class, FieldLength.random(0, 1000000));

        return configuration;
    }

    public static void reset() {
        INSTANCE.configuration.clear();
        INSTANCE.configuration.putAll(INSTANCE.defaultConfiguration());
    }

    public static void applyHints(ClassFactory factory) {
        INSTANCE.configuration.get(factory.getClass()).forEach(factory::putHint);
    }

    public static Configurer configure() {
        return INSTANCE;
    }
}
