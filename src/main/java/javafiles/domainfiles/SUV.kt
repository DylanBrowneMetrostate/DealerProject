package javafiles.domainfiles

/**
 * / **
 * SUV is a child class of [Vehicle]. Its constructor is called by [VehicleCreator.createVehicle].
 *
 * @author Christopher Engelhart
 */
class SUV
/**
 * Constructs a new [SUV] object.
 * Invokes the superclass constructor with "SUV" as the vehicle type,
 * and sets the vehicle ID.
 *
 * @param vehicleID The vehicle ID of the Pickup to be created.
 */
    (vehicleID: String, model: String, price: Long) : Vehicle("SUV", vehicleID, model, price)
