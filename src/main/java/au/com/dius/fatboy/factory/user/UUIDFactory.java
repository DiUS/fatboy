package au.com.dius.fatboy.factory.user;

import au.com.dius.fatboy.factory.impl.AbstractClassFactory;

import java.lang.reflect.Field;
import java.util.UUID;

public class UUIDFactory extends AbstractClassFactory<UUID> {
    @Override
    public boolean supports(Class clazz) {
        return clazz == UUID.class;
    }

    @Override
    public UUID create(Field field) {
        return UUID.randomUUID();
    }

}
