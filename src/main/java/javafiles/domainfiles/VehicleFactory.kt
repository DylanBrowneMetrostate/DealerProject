package javafiles.domainfiles

import javafiles.Key
import javafiles.customexceptions.InvalidPriceException
import javafiles.customexceptions.InvalidVehicleTypeException
import javafiles.customexceptions.MissingCriticalInfoException

/**
 * Defines the contract for a factory that creates [Vehicle] objects.
 * Implementations of this interface are responsible for instantiating specific
 * types of vehicles based on a provided type string.
 */
interface VehicleFactory {
    fun fillVehicle(v: Vehicle?, make: String?, date: Long?, priceUnit: String?, rentalStatus: Boolean?)

    @Throws(InvalidVehicleTypeException::class, InvalidPriceException::class, MissingCriticalInfoException::class)
    fun createVehicle(type: String?, id: String?, model: String?, price: Long?): Vehicle

    @Throws(InvalidVehicleTypeException::class, InvalidPriceException::class, MissingCriticalInfoException::class)
    fun createVehicle(map: Map<Key, Any>): Vehicle
}