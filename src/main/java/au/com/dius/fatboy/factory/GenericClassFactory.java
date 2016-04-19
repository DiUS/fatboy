package au.com.dius.fatboy.factory;

import au.com.dius.fatboy.factory.config.FactoryHint;

import java.lang.reflect.Type;

public interface GenericClassFactory<T> extends ClassFactory<T> {
    /**
     * Checks if this ClassFactory supports the generic creation of clazz, given its possible generic type genericType
     * Generic type has 3 possible values
     *
     * @param clazz       The class to create
     * @param genericType The generic type of the declared class, which has 3 possible value types:
     * @return {@code true} if this provider can create an instance (or generic instance) of the class in question, or {@code false} otherwise
     */
    boolean supports(Class clazz, Type genericType);

    /**
     * Creates an instance of {@code rawType}, given its actual type arguments {@code actualTypeArguments}
     * See {@code CollectionFactory} for an example of generic type instantiation
     *
     * @param rawType             The raw class of the type being created. (Map, List, Set etc)
     * @param actualTypeArguments the declared generic types for the class being created
     * @return A new object of class type {@code T} that this class is a factory for
     * @see au.com.dius.fatboy.factory.collections.CollectionFactory
     */
    T create(Class rawType, Type[] actualTypeArguments);

    /**
     * @param hint the new FactoryConfig for this factory
     */
    void putHint(FactoryHint hint);
}
