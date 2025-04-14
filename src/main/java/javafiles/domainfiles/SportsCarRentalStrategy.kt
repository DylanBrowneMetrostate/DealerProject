package javafiles.domainfiles

import javafiles.customexceptions.SportsCarRentalNotAllowedException

/**
 * The `SportsCarRentalStrategy` class implements the [RentalStrategy] interface
 * and defines the rental behavior for sports cars. Sports cars are not allowed to be rented,
 * therefore, any attempt to enable or disable rental for a sports car will result in a
 * [SportsCarRentalNotAllowedException] being thrown.
 */
class SportsCarRentalStrategy : RentalStrategy {
    /**
     * Attempts to enable the rental status of a sports car, which is not allowed.
     *
     * @param vehicle The [Vehicle] of type [SportsCar] to enable for rental.
     * @throws SportsCarRentalNotAllowedException Always thrown, to indicate that sports cars cannot be rented.
     */
    @Throws(SportsCarRentalNotAllowedException::class)
    override fun enableRental(vehicle: Vehicle?) {
        throw SportsCarRentalNotAllowedException("Sports cars cannot be rented.")
    }

    /**
     * Attempts to disable the rental status of a sports car, which is not allowed.
     *
     * @param vehicle The [Vehicle] of type [SportsCar] to disable the rental.
     * @throws SportsCarRentalNotAllowedException Always thrown, to indicate that sports cars cannot be rented.
     */
    @Throws(SportsCarRentalNotAllowedException::class)
    override fun disableRental(vehicle: Vehicle?) {
        throw SportsCarRentalNotAllowedException("Sports car rental features cannot be changed")
    }
}
