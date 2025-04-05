package javafiles.customexceptions;

/**
 * Exception thrown to indicate that an invalid or unsupported vehicle type was provided.
 */
public class InvalidVehicleTypeException extends Exception {

    /**
     * Constructs a new InvalidVehicleTypeException with the specified detail message.
     *
     * @param message The detailed message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public InvalidVehicleTypeException(String message) {
        super(message);
    }
}