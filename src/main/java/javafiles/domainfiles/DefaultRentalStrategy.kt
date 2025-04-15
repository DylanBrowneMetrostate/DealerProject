package javafiles.domainfiles

/**
 * The `DefaultRentalStrategy` class implements the [RentalStrategy] interface
 * and provides the default rental behavior for most vehicle types (excluding sports cars).
 * This strategy simply sets the vehicle's rental status to true when enabling rental and
 * to false when disabling rental.
 */
class DefaultRentalStrategy : RentalStrategy {
    /**
     * Enables the rental status of the specified [Vehicle] by setting its rental status to true.
     *
     * @param vehicle The [Vehicle] to enable for rental.
     */
    override fun enableRental(vehicle: Vehicle?) {
        vehicle?.setRental(true)
    }


    /**
     * Disables the rental status of the specified [Vehicle] by setting its rental status to false.
     *
     * @param vehicle The [Vehicle] to disable for rental.
     */
    override fun disableRental(vehicle: Vehicle?) {
        vehicle?.setRental(false)
    }
}