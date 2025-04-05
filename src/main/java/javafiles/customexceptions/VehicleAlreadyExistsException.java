package javafiles.customexceptions;

/**
 * Exception class representing the error when a vehicle object that already exists
 * in an inventory is attempted to be added again.

 */
public class VehicleAlreadyExistsException extends Exception {

    /**
     * Constructs a new VehicleAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     */
    public VehicleAlreadyExistsException(String message) {
        super(message);
    }
}
