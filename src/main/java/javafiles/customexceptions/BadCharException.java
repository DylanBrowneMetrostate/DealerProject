package javafiles.customexceptions;

public class BadCharException extends RuntimeException {
    public BadCharException(String message) {
        super(message);
    }
}
