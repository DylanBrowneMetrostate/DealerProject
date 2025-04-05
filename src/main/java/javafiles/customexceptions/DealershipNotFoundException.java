package javafiles.customexceptions;

/**
 * Exception class representing the error for when a {@link javafiles.domainfiles.Dealership}
 * is not found during searching
 */
public class DealershipNotFoundException extends Exception {

    /**
     * Constructs a new DealershipNotFoundException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     */
    public DealershipNotFoundException(String message) {
        super(message);
    }
}
