package javafiles.domainfiles

/**
 * Pickup is a child class of [Vehicle]. Its constructor is called by [VehicleCreator.createVehicle].
 *
 * @author Christopher Engelhart
 */
class Pickup
/**
 * Constructs a new [Pickup] object.
 * Invokes the superclass constructor with "Pickup" as the vehicle type,
 * and sets the vehicle ID.
 *
 * @param vehicleID The vehicle ID of the Pickup to be created.
 */
    (vehicleID: String, model: String, price: Long) : Vehicle("Pickup", vehicleID, model, price)
