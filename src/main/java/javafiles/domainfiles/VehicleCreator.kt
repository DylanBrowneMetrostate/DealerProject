package javafiles.domainfiles

import javafiles.Key
import javafiles.customexceptions.InvalidPriceException
import javafiles.customexceptions.InvalidVehicleTypeException
import javafiles.customexceptions.MissingCriticalInfoException
import javafiles.customexceptions.SportsCarRentalNotAllowedException
import java.util.*

/**
 * A factory class for creating [Vehicle] objects.
 * Implements the Singleton pattern to ensure only one instance exists.
 */
class VehicleCreator private constructor() // Private constructor
    : VehicleFactory {
    /**
     * Takes a setter method from [Vehicle] and calls the method so long as val is not null.
     * If val is null, setter is not called and the value of the parameter in vehicle is not changed.
     *
     * @param vehicle The [Vehicle] who's value is being set.
     * @param value The value that is being set
     * @param setter The setter method in [Vehicle] that is being called.
     * @param [T] The type of parameter that is being set by the setter.
     */
    private fun <T> setIfNotNull(vehicle: Vehicle?, value: T?, setter: (Vehicle?, T) -> Unit) {
        if (value != null) {
            setter(vehicle, value)
        }
    }

    override fun fillVehicle(
        vehicle: Vehicle?,
        make: String?,
        date: Long?,
        priceUnit: String?,
        rentalStatus: Boolean?
    ) {
        setIfNotNull(vehicle, make) { v, m -> v?.vehicleManufacturer = m }
        setIfNotNull(vehicle, date) { v, d -> v?.acquisitionDate = d }
        setIfNotNull(vehicle, priceUnit) { v, u -> v?.priceUnit = u }
        setIfNotNull(vehicle, rentalStatus) { v, state -> v?.rentalStatus = state }
    }

    /**
     * Creates a [Vehicle] object based on the given vehicle type.
     *
     * @param type The type of vehicle to create ("suv", "sedan", "pickup", "sports car").
     * @param id The ID of the vehicle to create.
     * @return A [Vehicle] object of the specified type.
     * @throws InvalidVehicleTypeException If the vehicle type is not supported.
     */
    override fun createVehicle(type: String?, id: String?, model: String?, price: Long?): Vehicle {
        if (type == null) {
            throw InvalidVehicleTypeException("Null Vehicle type.")
        }
        if (id.isNullOrBlank()) {
            throw MissingCriticalInfoException("Null Vehicle id.")
        }
        if (model.isNullOrBlank()) {
            throw MissingCriticalInfoException("Null Vehicle model.")
        }
        if (price == null) {
            throw InvalidPriceException("Null Vehicle price.")
        }
        if (price <= 0) {
            throw InvalidPriceException("Price is invalid ($price <= 0).")
        }

        return when (type.lowercase(Locale.getDefault())) {
            "suv" -> SUV(id, model, price)
            "sedan" -> Sedan(id, model, price)
            "pickup" -> Pickup(id, model, price)
            "sports car" -> SportsCar(id, model, price)
            else -> throw InvalidVehicleTypeException("$type is not a valid Vehicle Type.")
        }
    }


    /**
     * Creates a [Vehicle] object from a map of key-value pairs.
     *
     *
     * This method extracts the necessary vehicle information (type, ID, model, price, make, acquisition date,
     * price unit, and rental status) from the provided map and creates a [Vehicle] object.
     * It then uses the [fillVehicle] method to set additional vehicle attributes.
     *
     * @param map A map containing the vehicle's attributes.
     * @return A [Vehicle] object created from the map's data.
     * @throws InvalidVehicleTypeException If the vehicle type is invalid.
     * @throws InvalidPriceException If the price is invalid.
     * @throws MissingCriticalInfoException If critical information (type, id, model) is missing.
     * @throws SportsCarRentalNotAllowedException If [SportsCar] is marked as rented.
     */
    @Throws(InvalidVehicleTypeException::class, InvalidPriceException::class,
            MissingCriticalInfoException::class, SportsCarRentalNotAllowedException::class)
    override fun createFullVehicle(map: Map<Key, Any>): Vehicle {
        val type = map[Key.VEHICLE_TYPE] as? String
        val id = map[Key.VEHICLE_ID] as? String
        val model = map[Key.VEHICLE_MODEL] as? String
        val price = map[Key.VEHICLE_PRICE] as? Long

        val vehicle = createVehicle(type, id, model, price)

        val make = map[Key.VEHICLE_MANUFACTURER] as? String
        val date = map[Key.VEHICLE_ACQUISITION_DATE] as? Long
        val unit = map[Key.VEHICLE_PRICE_UNIT] as? String
        var rentalStatus = map[Key.VEHICLE_RENTAL_STATUS] as? Boolean

        if (rentalStatus != null && vehicle is SportsCar && rentalStatus == false) {
            rentalStatus = null
        }

        fillVehicle(vehicle, make, date, unit, rentalStatus)

        return vehicle
    }
    companion object {
        val instance: VehicleCreator = VehicleCreator()
    }
}