package au.com.dius.fatboy.factory.semantic;

import java.lang.reflect.Field;

public interface SemanticFieldFactory<T> {
    boolean supports(Field field);

    T create(Field field);
}
