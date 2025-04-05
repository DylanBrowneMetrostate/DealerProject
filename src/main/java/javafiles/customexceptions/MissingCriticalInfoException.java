package javafiles.customexceptions;

public class MissingCriticalInfoException extends Exception {
    /**
     * Creates a new ReadWriteException with the given message
     *
     * @param message The message that the exception has
     */
    public MissingCriticalInfoException(String message) {
        super(message);
    }
}
