package javafiles.domainfiles

/**
 * Sedan is a child class of [Vehicle]. Its constructor is called by [VehicleCreator.createVehicle]
 */
class Sedan
/**
 * Constructs a new Sedan object.
 * Invokes the superclass constructor with "Sedan" as the vehicle type,
 * and sets the vehicle ID.
 *
 * @param vehicleID The vehicle ID of the Pickup to be created.
 */
(vehicleID: String, model: String, price: Long) : Vehicle("Sedan", vehicleID, model, price)
