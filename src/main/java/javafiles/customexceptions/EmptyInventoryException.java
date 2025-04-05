package javafiles.customexceptions;

/**
 * An Exception class representing the error for when a vehicle is being removed
 * from an empty inventory
 *
 */
public class EmptyInventoryException extends Exception {

    /**
     * Constructs a new EmptyInventoryException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     */
    public EmptyInventoryException(String message) {
        super(message);
    }
}
