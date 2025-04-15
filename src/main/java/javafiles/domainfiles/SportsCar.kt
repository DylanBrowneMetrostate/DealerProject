package javafiles.domainfiles

/**
 * SportsCar is a child class of [Vehicle]. It represents a sports car vehicle,
 * which has a special rental strategy that prevents it from being rented.
 * The constructor initializes the vehicle with the "Sports car" type and the
 * [SportsCarRentalStrategy]. SportsCar constructor is called by [VehicleCreator.createVehicle]
 */
class SportsCar
/**
 * Constructs a new [SportsCar] object.
 * Invokes the superclass constructor with "Sports car" as the vehicle type
 * ,sets the vehicleID and a [SportsCarRentalStrategy] to prevent rentals.
 *
 *
 * @param vehicleID The vehicle ID of the SportsCar to be created.
 */
(vehicleID: String, model: String, price: Long) : Vehicle("Sports car", vehicleID, model, price, SportsCarRentalStrategy())
