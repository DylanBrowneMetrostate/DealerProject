package javafiles.customexceptions;


/**
 * An exception class representing the error of when a vehicle is attempted to be rented but
 * the vehicle's renting feature has not been enabled.
 * This exception is a subclass of {@link RentalException}.
 */
public class VehicleNotRentableException extends RentalException {

    /**
     * Constructs a new VehicleNotRentableException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     */
    public VehicleNotRentableException(String message) {
        super(message);
    }
}
