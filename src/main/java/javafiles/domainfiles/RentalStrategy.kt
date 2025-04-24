package javafiles.domainfiles

import javafiles.customexceptions.RentalException

/**
 * The `RentalStrategy` interface defines the contract for implementing different rental strategies
 * for [Vehicle] objects. This interface allows for flexible and extensible rental management,
 * enabling the application to support various rental policies without modifying core [Vehicle] logic.
 */
interface RentalStrategy {
    /**
     * Notifies the [RentalStrategy] that the rental status is attempting to be changed, so
     * a [RentalException] can be thrown if applicable.
     *
     * @param value The attempted new value of the rental status.
     * @throws RentalException when the rental strategy prevents updating for the given values.
     */
    @Throws(RentalException::class)
    fun updateTo(value: Boolean)
}
