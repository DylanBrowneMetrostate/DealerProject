package javafiles.customexceptions;

/**
 * Exception class representing the error when a vehicle is not found in the inventory.
 */
public class VehicleNotFoundException extends RuntimeException {

    /**
     * Constructs a new VehicleNotFoundException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     */
    public VehicleNotFoundException(String message) {
        super(message);
    }
}
