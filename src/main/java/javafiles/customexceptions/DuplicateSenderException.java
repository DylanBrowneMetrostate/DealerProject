package javafiles.customexceptions;

/**
 * Exception class representing the error for when a dealership is attempting to transfer
 * vehicles to itself.
 */
public class DuplicateSenderException extends Exception
{
    /**
     * Constructs a new DuplicateSenderException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     */
    public DuplicateSenderException(String message) {
        super(message);
    }
}
