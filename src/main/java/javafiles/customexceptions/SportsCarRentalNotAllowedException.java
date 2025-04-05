package javafiles.customexceptions;

/**
 * Exception class representing the error when a sports car rental is not allowed.
 * This exception is a subclass of {@link RentalException} and specifically indicates
 * that the requested rental of a sports car is prohibited due company policy.
 */
public class SportsCarRentalNotAllowedException extends RentalException {

    /**
     * Constructs a new SportsCarRentalNotAllowedException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     */
    public SportsCarRentalNotAllowedException(String message) {
        super(message);
    }
}