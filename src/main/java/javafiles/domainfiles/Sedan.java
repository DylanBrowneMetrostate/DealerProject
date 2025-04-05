package javafiles.domainfiles;

/**
 * Sedan is a child class of Vehicle. Its constructor is called by {@link {@link VehicleCreator#createVehicle(String, String, String, Long)}.
 *
 * @author Christopher Engelhart
 */

public class Sedan extends Vehicle
{
    /**
     * Constructs a new Sedan object.
     * Invokes the superclass constructor with "Sedan" as the vehicle type,
     * and sets the vehicle ID using {@link Vehicle#setVehicleId(String)}.
     *
     * @param vehicleID The vehicle ID of the Pickup to be created.
     */
    Sedan(String vehicleID, String model, Long price) {
        super("Sedan", vehicleID, model, price);
    }
}