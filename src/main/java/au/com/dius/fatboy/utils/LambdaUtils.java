package au.com.dius.fatboy.utils;

import java.util.function.Consumer;

public class LambdaUtils {
    public static <T> Consumer<T> unchecked(UncheckedConsumer<T> delegate) {
        return (incoming) -> {
            try {
                delegate.call(incoming);
            } catch (Exception e) {
                throw (RuntimeException) e;
            }
        };
    }

    public static <T> Consumer uncheck(UncheckedVoid delegate) {
        return (incoming) -> {
            try {
                delegate.call();
            } catch (Exception e) {
                throw (RuntimeException) e;
            }
        };
    }

    public static <T> T unchecked(UncheckedSupplier<T> t) {
        try {
            return t.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FunctionalInterface
    public static interface UncheckedConsumer<T> {
        void call(T t) throws Exception;
    }

    @FunctionalInterface
    public static interface UncheckedVoid {
        void call() throws Exception;
    }

    @FunctionalInterface
    public static interface UncheckedSupplier<T> {
        T call() throws Exception;
    }
}
