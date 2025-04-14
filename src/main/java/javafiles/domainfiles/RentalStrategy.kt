package javafiles.domainfiles;

import javafiles.customexceptions.RentalException;

/**
 * The {@code RentalStrategy} interface defines the contract for implementing different rental strategies
 * for {@link Vehicle} objects. This interface allows for flexible and extensible rental management,
 * enabling the application to support various rental policies without modifying core vehicle logic.
 */
public interface RentalStrategy {

    /**
     * Enables the rental status of the specified {@link Vehicle}.
     *
     * <p>This method should perform all necessary actions to mark the vehicle as rented, such as updating
     * the vehicle's rental status indicating that it's currently rented.
     *
     * @param vehicle The {@link Vehicle} to enable for rental.
     * @throws RentalException If an error occurs during the rental enabling process.
     */
    void enableRental(Vehicle vehicle) throws RentalException;


    /**
     * Enables the rental status of the specified {@link Vehicle}.
     *
     * <p>This method should perform all necessary actions to mark the vehicle as not currently
     * rented by changing the vehicle's rental status.
     *
     * @param vehicle The {@link Vehicle} to enable for rental.
     * @throws RentalException If an error occurs during the rental enabling process.
     */
    void disableRental(Vehicle vehicle) throws RentalException;

}
