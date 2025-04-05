package javafiles.customexceptions;

/**
 * An exception class representing the error of when a dealership not currently providing
 * rental services attempts to add a vehicle to their rental inventory.
 * This exception is a subclass of {@link RentalException}.
 */
public class DealershipNotRentingException extends RentalException{

    /**
     * Constructs a new DealershipNotRentingException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     */
    public DealershipNotRentingException(String message) {
        super(message);
    }
}
