package au.com.dius.fatboy.factory;

import au.com.dius.fatboy.factory.config.FactoryConfig;

import java.lang.reflect.Field;

public interface ClassFactory<T> {

    /**
     * Checks if this ClassFactory is targetted at a specific Class
     *
     * @param clazz The class to check support against
     */
    boolean supports(Class clazz);

    /**
     * Checks if this ClassFactory is targetted at a specific field (can check for generics, annotations etc)
     *
     * @param field The field to check support against
     */
    boolean supports(Field field);

    /**
     * @param field The field to create an instance of
     */
    T create(Field field);

    /**
     * @param config the new FactoryConfig for this factory
     */
    void setConfig(FactoryConfig config);
}
