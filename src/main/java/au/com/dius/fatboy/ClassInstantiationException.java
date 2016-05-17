package au.com.dius.fatboy;

public class ClassInstantiationException extends RuntimeException {

    public ClassInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassInstantiationException(String message) {
        super(message);
    }
}
