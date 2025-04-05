package javafiles.customexceptions;

/**
 * Exception thrown when a vehicle price string cannot be parsed into a Long.
 */
public class InvalidLongPriceException extends Exception {

    /**
     * Constructs an InvalidVehiclePriceException with the specified detail message.
     *
     * @param message The detail message.
     */
    public InvalidLongPriceException(String message) {
        super(message);
    }
}
