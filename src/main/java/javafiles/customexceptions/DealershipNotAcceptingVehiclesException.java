package javafiles.customexceptions;

/**
 * Exception class representing the error for when a vehicle is attempted
 * to be added to a dealership that is not currently accepting new vehicles.
 */
public class DealershipNotAcceptingVehiclesException extends Exception {

    /**
     * Constructs a new DealershipNotAcceptingVehicleException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     */
    public DealershipNotAcceptingVehiclesException(String message) {
        super(message);
    }
}
