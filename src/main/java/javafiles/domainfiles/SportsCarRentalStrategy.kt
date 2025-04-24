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
     * Notifies the [RentalStrategy] that the rental status is attempting to be changed.
     * Since [SportsCarRentalStrategy] never allows the changing of rental statuses,
     * a [SportsCarRentalNotAllowedException] is always thrown.
     *
     * @param value The attempted new value of the rental status.
     * @throws SportsCarRentalNotAllowedException always.
     */
    @Throws(SportsCarRentalNotAllowedException::class)
    override fun updateTo(value: Boolean) {
        throw SportsCarRentalNotAllowedException("Sports car rental features cannot be changed")
    }
}
