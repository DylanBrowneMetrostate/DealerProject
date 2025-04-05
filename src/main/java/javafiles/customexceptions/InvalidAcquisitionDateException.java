package javafiles.customexceptions;

/**
 * Exception thrown when an acquisition date string cannot be parsed into a Long.
 */
public class InvalidAcquisitionDateException extends Exception {
    /**
     * Constructs an InvalidAcquisitionDateException with the specified detail message.
     *
     * @param message The detail message.
     */
    public InvalidAcquisitionDateException(String message) {
        super(message);
    }
}