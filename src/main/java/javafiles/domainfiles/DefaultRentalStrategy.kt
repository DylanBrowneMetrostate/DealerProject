package javafiles.domainfiles

import javafiles.customexceptions.RentalException

/**
 * The `DefaultRentalStrategy` class implements the [RentalStrategy] interface
 * and provides the default rental behavior for most vehicle types (excluding sports cars).
 * This strategy simply sets the vehicle's rental status to true when enabling rental and
 * to false when disabling rental.
 */
class DefaultRentalStrategy : RentalStrategy {
    /**
     * Notifies [DefaultRentalStrategy] that the rental status is attempting to be changed.
     * The default strategy is to allow all changes, so the method does not throw a [RentalException].
     *
     * @param value The attempted new value of the rental status.
     */
    override fun updateTo(value: Boolean) { }
}