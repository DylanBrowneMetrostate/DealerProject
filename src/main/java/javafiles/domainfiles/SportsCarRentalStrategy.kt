package javafiles.domainfiles;

import javafiles.customexceptions.SportsCarRentalNotAllowedException;

/**
 * The {@code SportsCarRentalStrategy} class implements the {@link RentalStrategy} interface
 * and defines the rental behavior for sports cars. Sports cars are not allowed to be rented,
 * therefore, any attempt to enable or disable rental for a sports car will result in a
 * {@link SportsCarRentalNotAllowedException} being thrown.
 */
public class SportsCarRentalStrategy implements RentalStrategy {

    /**
     * Attempts to enable the rental status of a sports car, which is not allowed.
     *
     * @param vehicle The {@link Vehicle} of type {@link SportsCar} to enable for rental.
     * @throws SportsCarRentalNotAllowedException Always thrown, to indicate that sports cars cannot be rented.
     */
    @Override
    public void enableRental(Vehicle vehicle) throws SportsCarRentalNotAllowedException {
        throw new SportsCarRentalNotAllowedException("Sports cars cannot be rented.");
    }

    /**
     * Attempts to disable the rental status of a sports car, which is not allowed.
     *
     * @param vehicle The {@link Vehicle} of type {@link SportsCar} to disable the rental.
     * @throws SportsCarRentalNotAllowedException Always thrown, to indicate that sports cars cannot be rented.
     */
    @Override
    public void disableRental(Vehicle vehicle) throws SportsCarRentalNotAllowedException
    {
        throw  new SportsCarRentalNotAllowedException("Sports car rental features cannot be changed");
    }
}
