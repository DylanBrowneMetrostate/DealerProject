package javafiles.customexceptions;

/**
 * Exception thrown when a Dealership is not selected to modify
 */
public class DealershipNotSelectedException extends Exception {

    /**
     * Constructs a DealershipNotSelectedException with the specified detail message.
     *
     * @param message The detail message.
     */
    public DealershipNotSelectedException(String message) {
        super(message);
    }
}
