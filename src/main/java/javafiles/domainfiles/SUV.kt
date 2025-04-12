package javafiles.domainfiles;

/**
 /**
 * SUV is a child class of Vehicle. Its constructor is called by {@link VehicleCreator#createVehicle(String, String, String, Long)}.
 *
 * @author Christopher Engelhart
 */

public class SUV extends Vehicle
{
    /**
     * Constructs a new SUV object.
     * Invokes the superclass constructor with "SUV" as the vehicle type,
     * and sets the vehicle ID using {@link Vehicle#setVehicleId(String)}.
     *
     * @param vehicleID The vehicle ID of the Pickup to be created.
     */
    public SUV(String vehicleID, String model, Long price) {
        super("SUV", vehicleID, model, price);
    }

}