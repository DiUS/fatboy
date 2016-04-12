package au.com.dius.fatboy.factory.impl;

import au.com.dius.fatboy.factory.ClassFactory;
import au.com.dius.fatboy.factory.config.FactoryConfig;

import java.lang.reflect.Field;

public abstract class AbstractClassFactory<T> implements ClassFactory<T> {
    private FactoryConfig config;

    public AbstractClassFactory() {
        this(null);
    }

    public AbstractClassFactory(FactoryConfig config) {
        this.config = config;
    }

    public <P extends FactoryConfig> P getConfig(Class<P> clazz) {
        return (P) config;
    }

    public void setConfig(FactoryConfig config) {
        this.config = config;
    }

    @Override
    public boolean supports(Field field) {
        return supports(field.getType());
    }
}
