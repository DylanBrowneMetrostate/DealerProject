package javafiles.domainfiles;

import javafiles.Key;
import javafiles.customexceptions.InvalidPriceException;
import javafiles.customexceptions.InvalidVehicleTypeException;
import javafiles.customexceptions.MissingCriticalInfoException;

import java.util.Map;
import java.util.function.BiConsumer;

/**
 * A factory class for creating {@link Vehicle} objects.
 * Implements the Singleton pattern to ensure only one instance exists.
 */
class VehicleCreator implements VehicleFactory {

    private static final VehicleCreator instance = new VehicleCreator();

    private VehicleCreator() {} // Private constructor

    public static VehicleCreator getInstance() {
        return instance;
    }

    /**
     * Takes a setter method from {@link Vehicle} and calls the method so long as val is not null.
     * If val is null, setter is not called and the value of the parameter in vehicle is not changed.
     *
     * @param vehicle The {@link Vehicle} who's value is being set.
     * @param val The value that is being set
     * @param setter The setter method in {@link Vehicle} that is being called.
     * @param <T> The type of parameter that is being set by the setter.
     */
    private <T> void setIfNotNull(Vehicle vehicle, T val, BiConsumer<Vehicle, T> setter) {
        if (val != null) {
            setter.accept(vehicle, val);
        }
    }

    @Override
    public void fillVehicle(Vehicle vehicle, String make, Long date, String priceUnit, Boolean rentalStatus) {
        setIfNotNull(vehicle, make, Vehicle::setVehicleManufacturer);
        setIfNotNull(vehicle, date, Vehicle::setAcquisitionDate);
        setIfNotNull(vehicle, priceUnit, Vehicle::setPriceUnit);
        setIfNotNull(vehicle, rentalStatus, Vehicle::setRental);
    }

    /**
     * Creates a {@link Vehicle} object based on the given vehicle type.
     *
     * @param type The type of vehicle to create ("suv", "sedan", "pickup", "sports car").
     * @param id The ID of the vehicle to create.
     * @return A {@link Vehicle} object of the specified type.
     * @throws InvalidVehicleTypeException If the vehicle type is not supported.
     */
    @Override
    public Vehicle createVehicle(String type, String id, String model, Long price)
            throws InvalidVehicleTypeException, InvalidPriceException, MissingCriticalInfoException {
        if (type == null) {throw new InvalidVehicleTypeException("Null Vehicle type.");}
        if (id == null || id.isBlank()) {throw new MissingCriticalInfoException("Null Vehicle id.");}
        if (model == null || model.isBlank()) {throw new MissingCriticalInfoException("Null Vehicle model.");}
        if (price == null) {throw new InvalidPriceException("Null Vehicle price.");}
        if (price <= 0) {throw new InvalidPriceException("Price is invalid (" + price + " <= 0).");}

        return switch (type.toLowerCase()) {
            case "suv" -> new SUV(id, model, price);
            case "sedan" -> new Sedan(id, model, price);
            case "pickup" -> new Pickup(id, model, price);
            case "sports car" -> new SportsCar(id, model, price);
            default ->  throw new InvalidVehicleTypeException(type + " is not a valid Vehicle Type.");
        };
    }


    /**
     * Creates a {@link Vehicle} object from a map of key-value pairs.
     *
     * <p>This method extracts the necessary vehicle information (type, ID, model, price, make, acquisition date,
     * price unit, and rental status) from the provided map and creates a {@link Vehicle} object.
     * It then uses the {@link #fillVehicle(Vehicle, String, Long, String, Boolean)} method to set additional
     * vehicle attributes.</p>
     *
     * @param map A map containing the vehicle's attributes.
     * @return A {@link Vehicle} object created from the map's data.
     * @throws InvalidVehicleTypeException If the vehicle type is invalid.
     * @throws InvalidPriceException If the price is invalid.
     */
    @Override
    public Vehicle createVehicle(Map<Key, Object> map) throws InvalidVehicleTypeException,
            InvalidPriceException, MissingCriticalInfoException {

        String type = Key.VEHICLE_TYPE.getVal(map, String.class);
        String id = Key.VEHICLE_ID.getVal(map, String.class);
        String model = Key.VEHICLE_MODEL.getVal(map, String.class);
        Long price = Key.VEHICLE_PRICE.getVal(map, Long.class);

        Vehicle vehicle = createVehicle(type, id, model, price);

        String make = Key.VEHICLE_MANUFACTURER.getVal(map, String.class);
        Long date = Key.VEHICLE_ACQUISITION_DATE.getVal(map, Long.class);
        String unit = Key.VEHICLE_PRICE_UNIT.getVal(map, String.class);
        Boolean rentalStatus = Key.VEHICLE_RENTAL_STATUS.getVal(map, Boolean.class);

        fillVehicle(vehicle, make, date, unit, rentalStatus);

        return vehicle;
    }
}