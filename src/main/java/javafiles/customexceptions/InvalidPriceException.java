package javafiles.customexceptions;

/**
 * Exception class for the error when a price for a vehicle is set to a non-positive value.
 */
public class InvalidPriceException extends Exception {

    /**
     * Constructs a new InvalidPriceException with the specified detail message.
     *
     * @param message The detailed message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public InvalidPriceException(String message) {
        super(message);
    }
}
