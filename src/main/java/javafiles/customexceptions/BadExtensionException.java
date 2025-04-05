package javafiles.customexceptions;

public class BadExtensionException extends RuntimeException {
    public BadExtensionException(String message) {
        super(message);
    }
}
