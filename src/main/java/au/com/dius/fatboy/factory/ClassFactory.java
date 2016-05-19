package au.com.dius.fatboy.factory;

import au.com.dius.fatboy.factory.config.FactoryHint;

import java.lang.reflect.Field;

public interface ClassFactory<T> {

    /**
     * Checks if this ClassFactory is targetted at a specific Class
     *
     * @param clazz The class to check support against
     * @return {@code true} if this {@code ClassFactory} supports the clazz requested to be created, {@code false} otherwise
     */
    boolean supports(Class clazz);

    /**
     * Checks if this ClassFactory is targetted at a specific field (can check for generics, annotations etc)
     *
     * @param field The field to check support against
     * @return {@code true} if this {@code ClassFactory} supports the field requested to be created, {@code false} otherwise
     */
    boolean supports(Field field);

    /**
     * @param field The field to create an instance of
     * @param clazz the class of the object to be created. For use when field is unknown and object is part of an inheritance tree
     * @return A new object of class type {@code T} that this class is a factory for
     */
    T create(Field field, Class<?> clazz);

    /**
     * @param hint a new FactoryHint. If any FactoryHint with the same class exists, it should be replaced by this one.
     */
    void putHint(FactoryHint hint);

    /**
     * @param clazz The subclass of FactoryHint you want to remove
     * @param <P> a {@code FactoryHint} subclass
     */
    public <P extends FactoryHint> void removeHint(Class<P> clazz);
}
