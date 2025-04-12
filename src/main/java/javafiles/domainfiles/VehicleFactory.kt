package javafiles.domainfiles;

import javafiles.Key;
import javafiles.customexceptions.InvalidPriceException;
import javafiles.customexceptions.InvalidVehicleTypeException;
import javafiles.customexceptions.MissingCriticalInfoException;

import java.util.Map;

/**
 * Defines the contract for a factory that creates {@link Vehicle} objects.
 * Implementations of this interface are responsible for instantiating specific
 * types of vehicles based on a provided type string.
 */
interface VehicleFactory {
    void fillVehicle(Vehicle v, String make, Long date, String priceUnit, Boolean rentalStatus);
    Vehicle createVehicle(String type, String id, String model, Long price)
            throws InvalidVehicleTypeException, InvalidPriceException, MissingCriticalInfoException;
    Vehicle createVehicle(Map<Key, Object> map) throws InvalidVehicleTypeException,
            InvalidPriceException, MissingCriticalInfoException;
}