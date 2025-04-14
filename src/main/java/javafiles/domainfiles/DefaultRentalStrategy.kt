package javafiles.domainfiles;

/**
 * The {@code DefaultRentalStrategy} class implements the {@link RentalStrategy} interface
 * and provides the default rental behavior for most vehicle types (excluding sports cars).
 * This strategy simply sets the vehicle's rental status to true when enabling rental and
 * to false when disabling rental.
 */
public class DefaultRentalStrategy implements RentalStrategy {

    /**
     * Enables the rental status of the specified {@link Vehicle} by setting its rental status to true.
     *
     * @param vehicle The {@link Vehicle} to enable for rental.
     */
    @Override
    public void enableRental(Vehicle vehicle) {
        vehicle.setRental(true);
    }


    /**
     * Disables the rental status of the specified {@link Vehicle} by setting its rental status to false.
     *
     * @param vehicle The {@link Vehicle} to disable for rental.
     */
    @Override
    public void disableRental(Vehicle vehicle)
    {
        vehicle.setRental(false);
    }
}