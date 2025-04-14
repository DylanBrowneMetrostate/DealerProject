package javafiles.domainfiles

import javafiles.customexceptions.RentalException

/**
 * The `RentalStrategy` interface defines the contract for implementing different rental strategies
 * for [Vehicle] objects. This interface allows for flexible and extensible rental management,
 * enabling the application to support various rental policies without modifying core vehicle logic.
 */
interface RentalStrategy {
    /**
     * Enables the rental status of the specified [Vehicle].
     *
     *
     * This method should perform all necessary actions to mark the vehicle as rented, such as updating
     * the vehicle's rental status indicating that it's currently rented.
     *
     * @param vehicle The [Vehicle] to enable for rental.
     * @throws RentalException If an error occurs during the rental enabling process.
     */
    @Throws(RentalException::class)
    fun enableRental(vehicle: Vehicle?)


    /**
     * Enables the rental status of the specified [Vehicle].
     *
     *
     * This method should perform all necessary actions to mark the vehicle as not currently
     * rented by changing the vehicle's rental status.
     *
     * @param vehicle The [Vehicle] to enable for rental.
     * @throws RentalException If an error occurs during the rental enabling process.
     */
    @Throws(RentalException::class)
    fun disableRental(vehicle: Vehicle?)
}
