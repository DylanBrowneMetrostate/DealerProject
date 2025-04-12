package javafiles.domainfiles;

/**
 * Pickup is a child class of Vehicle. Its constructor is called by {@link VehicleCreator#createVehicle(String, String, String, Long)}.
 *
 * @author Christopher Engelhart
 */

public class Pickup extends Vehicle
{
    /**
     * Constructs a new Pickup object.
     * Invokes the superclass constructor with "Pickup" as the vehicle type,
     * and sets the vehicle ID using {@link Vehicle#setVehicleId(String)}.
     *
     * @param vehicleID The vehicle ID of the Pickup to be created.
     */
    public Pickup(String vehicleID, String model, Long price) {
        super("Pickup", vehicleID, model, price);
    }

}