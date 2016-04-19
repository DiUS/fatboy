package au.com.dius.fatboy.factory.impl;

import au.com.dius.fatboy.factory.ClassFactory;
import au.com.dius.fatboy.factory.config.FactoryHint;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.lang.reflect.Field;
import java.util.Map;

public abstract class AbstractClassFactory<T> implements ClassFactory<T> {
    protected Map<Class<? extends FactoryHint>, FactoryHint> hints;

    public AbstractClassFactory(FactoryHint... hint) {
        hints = Maps.newHashMap();
        Lists.newArrayList(hint).forEach(x -> hints.put(x.getClass(), x));
    }

    public <P extends FactoryHint> P getHint(Class<P> clazz) {
        return (P) hints.get(clazz);
    }

    public void putHint(FactoryHint hint) {
        hints.put(hint.getClass(), hint);
    }

    public <P extends FactoryHint> void removeHint(Class<P> clazz) {
        hints.remove(clazz);
    }

    @Override
    public boolean supports(Field field) {
        return supports(field.getType());
    }
}
