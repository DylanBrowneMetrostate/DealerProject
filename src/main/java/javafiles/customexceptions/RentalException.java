package javafiles.customexceptions;

/**
 * Exception class representing errors related to rental operations.
 * This exception can be used to indicate various issues that arise
 * during the rental process, such as invalid vehicle type for rental, dealership not
 * currently renting.
 */
public class RentalException extends Exception {

    /**
     * Constructs a new RentalException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     */
    public RentalException(String message) {
        super(message);
    }

    /**
     * Constructs a new RentalException with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method).
     */
    public RentalException(String message, Throwable cause) {
        super(message, cause);
    }
}