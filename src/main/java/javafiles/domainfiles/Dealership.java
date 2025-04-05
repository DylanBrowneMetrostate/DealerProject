package javafiles.domainfiles;

import javafiles.Key;
import javafiles.customexceptions.*;

import java.util.*;

/**
 * Represents a dealership that manages vehicle sales and rentals.
 * <p>
 * This class provides functionality to add, remove, and retrieve vehicles from the dealership's
 * sales and rental inventories. It also allows for enabling and disabling vehicle acquisition
 * and rental services.
 * <p>
 * The dealership is identified by a unique dealer ID and maintains separate inventories for
 * vehicles available for sale and rental.
 * <p>
 * Authors: Patrick McLucas, Christopher Engelhart
 */

public class Dealership {
    private final String dealerId;
    private String name;
    private final ArrayList<Vehicle> salesInventory;
    private final ArrayList<Vehicle> rentalInventory;
    private static final VehicleFactory vehicleFactory = VehicleCreator.getInstance(); // Singleton
    private boolean receivingVehicle;
    private boolean rentingVehicles;

    /**
     * Constructs a new Dealership with the specified dealer ID and name.
     *
     * @param dealerId The unique identifier for the dealership.
     * @param name     The name of the dealership.
     */
    public Dealership(String dealerId, String name) {
        // necessary
        this.dealerId = dealerId;

        // defaults
        this.name = name;
        this.receivingVehicle = true;
        this.rentingVehicles = false;

        salesInventory = new ArrayList<>();
        rentalInventory = new ArrayList<>();
    }

    // Getters:
    public String getDealerId () {return dealerId;}
    public String getDealerName () {return name;}
    public boolean getStatusAcquiringVehicle() {return receivingVehicle;}
    public boolean getRentingVehicles() {return rentingVehicles;}
    public ArrayList<Vehicle> getSaleVehicles() {return salesInventory;}
    public ArrayList<Vehicle> getRentalVehicles() {return rentalInventory;}

    // Setters:
    public void setName(String name) {this.name = name;}
    public void setReceivingVehicle(Boolean status) {
        receivingVehicle = Objects.requireNonNullElse(status, true);
    }
    public void setRentingVehicles(Boolean status) {
        rentingVehicles = Objects.requireNonNullElse(status, false);
    }

    /**
     * Retrieves a vehicle from the sales inventory by its ID.
     *
     * @param vehicleID The ID of the vehicle to retrieve.
     * @return The Vehicle object.
     * @throws VehicleNotFoundException if the vehicle is not found.
     */
    public Vehicle getVehicleFromSalesInventory(String vehicleID) throws VehicleNotFoundException {
        for (Vehicle vehicle : salesInventory) {
            if (vehicle.getVehicleId()!= null && vehicle.getVehicleId().equals(vehicleID)) {
                return vehicle;
            }
        }
        throw new VehicleNotFoundException("Vehicle with ID: " + vehicleID + " not found in sales inventory.");
    }

    /**
     * Retrieves a vehicle from the rental inventory by its ID.
     *
     * @param vehicleID The ID of the vehicle to retrieve.
     * @return The Vehicle object.
     * @throws VehicleNotFoundException if the vehicle is not found.
     */
    public Vehicle getVehicleFromRentalInventory(String vehicleID) throws VehicleNotFoundException {
        for (Vehicle vehicle : rentalInventory) {
            if (vehicle.getVehicleId()!= null && vehicle.getVehicleId().equals(vehicleID)) {
                return vehicle;
            }
        }
        throw new VehicleNotFoundException("Vehicle with ID: " + vehicleID + " not found in rental inventory.");
    }

    /**
     * Checks if a vehicle is already present in the given inventory.
     *
     * @param newVehicle The vehicle to check for in the inventory.
     * @param inventory The inventory (list) where the vehicle might be located.
     * @return {@code true} if the vehicle is found in the inventory, {@code false} otherwise.
     *
     * @author Christopher Engelhart
     */
    private boolean isVehicleInInventory(Vehicle newVehicle, List<Vehicle> inventory) {

        for (Vehicle vehicle : inventory)
        {
            if (vehicle.getVehicleId().equals(newVehicle.getVehicleId()))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a vehicle is already present in the given {@link Dealership}.
     *
     * @param newId The id of the {@link Vehicle} to check for in the inventory.
     * @return {@code true} if the {@link Vehicle} is found in the inventory, {@code false} otherwise.
     */
    protected boolean isVehicleInInventoryById(String newId) {
        newId = newId.trim().replaceAll("\\s+", "");
        for (Vehicle vehicle : getTotalInventory()) {
            String existingVehicleId = vehicle.getVehicleId().trim().replaceAll("\\s+", "");
            if (existingVehicleId.equalsIgnoreCase(newId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a new vehicle to the dealership's sales inventory.
     * <p>
     * This method checks if the dealership is currently accepting new vehicles and if the vehicle is already
     * present in either the sales or rental inventory. If the dealership is not accepting new vehicles or if
     * the vehicle already exists, an exception is thrown.
     *
     * @param newVehicle The {@link Vehicle} object to be added to the inventory.
     * @throws DealershipNotAcceptingVehiclesException If the dealership is not currently accepting new vehicles.
     * @throws VehicleAlreadyExistsException If the vehicle is already present in either the sales or rental inventory.
     */
    public void addIncomingVehicle(Vehicle newVehicle) throws DealershipNotAcceptingVehiclesException,
            VehicleAlreadyExistsException
    {

        // Checks if the dealership is not accepting new vehicles
        if (!receivingVehicle) {
            throw new DealershipNotAcceptingVehiclesException("Dealership " + this.dealerId + " is not accepting new " +
                    "vehicles at this time. " + "Vehicle ID: " + newVehicle.getVehicleId() +
                    " was not added to Dealership: " + this.dealerId + ".");
        }

        if (isVehicleInInventory(newVehicle, salesInventory))
        {
            throw new VehicleAlreadyExistsException("This vehicle is already located in the sales inventory. Vehicle ID: "
                    + newVehicle.getVehicleId() + " was not added to dealership " + this.dealerId + ".");
        }

        if (isVehicleInInventory(newVehicle, rentalInventory))
        {
            throw new VehicleAlreadyExistsException("This vehicle is already located in the rental inventory. Vehicle ID: "
                    + newVehicle.getVehicleId() + " was not added to dealership " + this.dealerId + ".");
        }


        this.salesInventory.add(newVehicle);
    }

    /**
     * Takes a Map with information about a Vehicle, creates that Vehicle and adds to inventory.
     *
     * @param map The data needed to create the new Vehicle.
     * @return Returns true if the Vehicle was added, false otherwise.
     */
    public boolean dataToInventory(Map<Key, Object> map) {
        Vehicle vehicle;

        try {
            vehicle = vehicleFactory.createVehicle(map);
        } catch (InvalidVehicleTypeException | InvalidPriceException | MissingCriticalInfoException e) {
            ReadWriteException exception = new ReadWriteException(e);
            Key.REASON_FOR_ERROR.putValid(map, exception);
            return false;
        }

        try {
            addIncomingVehicle(vehicle);
        } catch (VehicleAlreadyExistsException | DealershipNotAcceptingVehiclesException e) {
            ReadWriteException exception = new ReadWriteException(e);
            Key.REASON_FOR_ERROR.putValid(map, exception);
            return false;
        }
        return true;
    }

    /**
     * Gets the complete inventory of dealership. A sum of all vehicles in rental and
     * sales inventory.
     *
     * @return totalInventory a total collection of target dealership's sales and rental inventory
     */
    public ArrayList<Vehicle> getTotalInventory()
    {
        ArrayList<Vehicle> totalInventory = new ArrayList<>();
        totalInventory.addAll(this.getSaleVehicles());
        totalInventory.addAll(this.getRentalVehicles());
        return totalInventory;
    }


    /**
     * Adds a new vehicle to the dealership inventory based on the provided vehicle details.
     * This method creates a new vehicle based on the vehicle type and sets its attributes
     * using the provided parameters.
     *</p>
     * {@link VehicleFactory#createVehicle(String, String, String, Long)} is used to create and validate the vehicle type.
     * If the vehicle type is unsupported, the method will print an error message and return without
     * making any changes to the inventory. If the vehicle is created successfully, it will be added
     * to the dealership's inventory using the {@link #addIncomingVehicle(Vehicle)} method.
     *
     *
     * @param vehicleId The unique identifier for the vehicle.
     * @param vehicleManufacturer The manufacturer of the vehicle.
     * @param vehicleModel The model of the vehicle.
     * @param vehiclePrice The price of the vehicle. The price must be a positive value representing the
     *                     cost of the vehicle.
     * @param acquisitionDate The date when the vehicle was acquired by the dealership.
     *                        @note acquisitionDate is a long value representing milliseconds
     *                        since the epoch.
     * @param vehicleType The type of the vehicle. This should be one of the following types:
     *                    "suv", "sedan", "pickup", or "sports car". If an unsupported type is provided,
     *                    the method will not add the vehicle and will print an error message.
     * @throws DealershipNotAcceptingVehiclesException If the dealership is not currently accepting new vehicles.
     * @throws VehicleAlreadyExistsException If the vehicle is already present in either the sales or rental inventory.
     * @throws InvalidVehicleTypeException If the vehicle type is not supported.
     * @throws InvalidPriceException if the vehicle price is not a positive value
     *
     * @author Christopher Engelhart
     */
    public void manualVehicleAdd(String vehicleId, String vehicleManufacturer, String vehicleModel, Long vehiclePrice,
                                 Long acquisitionDate, String vehicleType, String priceUnit) throws InvalidVehicleTypeException,
                                 VehicleAlreadyExistsException, DealershipNotAcceptingVehiclesException,
                                 InvalidPriceException, MissingCriticalInfoException {

        // Ensure the vehicle price is positive.
        if (vehiclePrice <= 0) {
            throw new InvalidPriceException("Error: Vehicle price must be a positive value. Vehicle ID: " + vehicleId + " was not added.");
        }

        Vehicle newVehicle = vehicleFactory.createVehicle(vehicleType, vehicleId, vehicleModel, vehiclePrice);

        vehicleFactory.fillVehicle(newVehicle, vehicleManufacturer, acquisitionDate, priceUnit, null);

        this.addIncomingVehicle(newVehicle);
    }

    /**
     * Updates the rental status of a vehicle within a dealership and moves it between
     * the dealership's sales and rental inventories based on the updated rental status.
     *
     * @param vehicle       The vehicle object with the updated rental status. This is the same vehicle object that
     * is present in the dealership's inventory (either sales or rental).
     * @throws RentalException       If the vehicle is a sports car, which is not rentable.
     */
    public void updateVehicleRental(Vehicle vehicle) throws RentalException {
        // Update the vehicle's rental status
        if (!vehicle.getVehicleType().equalsIgnoreCase("Sports car")) {
            if(vehicle.getRentalStatus())
            {
                vehicle.disableRental();
            }
            else
            {
                vehicle.enableRental();
            }
        }

        else {
            throw new VehicleNotRentableException("Sports car types are not currently rentable");
        }

        // Remove from the source inventory and add vehicle to opposite inventory
        if (this.getSaleVehicles().contains(vehicle)) {
            this.getSaleVehicles().remove(vehicle);
            this.getRentalVehicles().add(vehicle);
        } else {
            this.getRentalVehicles().remove(vehicle);
            this.getSaleVehicles().add(vehicle);
        }
    }

    /**
     * Adds a vehicle to the dealership's rental inventory.
     *
     * @param rental The vehicle to add to the rental inventory. Cannot be null.
     *
     * @throws IllegalArgumentException If the rental parameter is null.
     * @throws VehicleAlreadyExistsException If the vehicle is already in the rental inventory.
     * @throws DealershipNotRentingException If the dealership does not currently provide rental services.
     * @throws VehicleNotRentableException If the vehicle is not currently rentable.
     *
     * @author Christopher Engelhart
     */
    public void addRentalVehicle(Vehicle rental) throws IllegalArgumentException,VehicleAlreadyExistsException,
            DealershipNotRentingException, VehicleNotRentableException {

        if (rental == null) {
            throw new IllegalArgumentException("Rental vehicle is null.");
        }

        if (!this.getRentingVehicles()) {
            throw new DealershipNotRentingException("Dealership " + this.getDealerId() + " is not currently providing rental services.");
        }

        if (!rental.getRentalStatus()) {
            throw new VehicleNotRentableException("Vehicle " + rental.getVehicleId() + " is not currently rentable.");
        }

        if (this.isVehicleInInventory(rental,this.rentalInventory)) {
            throw new VehicleAlreadyExistsException("Vehicle " + rental.getVehicleId() + " is already in the rental inventory.");
        }

        this.rentalInventory.add(rental);
    }

    /**
     * Retrieves Vehicle data for the Dealership.
     * <p>
     * This method generates a List of Maps, where each Map represents a Vehicle
     * in the specified Dealership's inventory. Each Map contains key-value pairs
     * representing the vehicle's attributes.
     *
     *@return {@link List} of {@link Map} Objects where each Map object holds a specific vehicle
     *         and its data.(dealership ID, vehicle type, manufacturer, model,
     *         vehicle ID, price, and acquisition date) as key-value pairs.
     */
    public List<Map<Key, Object>> getDataMap() {
        List<Map<Key, Object>> list = new ArrayList<>();

        List<Vehicle> fullInventory = this.getTotalInventory();

        for (Vehicle vehicle: fullInventory) {
            Map<Key, Object> map = new HashMap<>();
            Key.DEALERSHIP_ID.putValid(map, dealerId);
            Key.DEALERSHIP_NAME.putValid(map, name);
            Key.DEALERSHIP_RECEIVING_STATUS.putValid(map, receivingVehicle);
            Key.DEALERSHIP_RENTING_STATUS.putValid(map, rentingVehicles);
            vehicle.getDataMap(map);
            list.add(map);
        }
        return list;
    }

    /**
     * Removes a vehicle from the dealership's inventory, including sales and rental.
     * Returns true if vehicle is removed and false otherwise.
     *
     * @param targetVehicle The vehicle to remove. Cannot be null.
     * @throws IllegalArgumentException If the {@code targetVehicle} is null.
     * @author Christopher Engelhart
     */
    public void removeVehicleFromInventory(Vehicle targetVehicle) throws IllegalArgumentException {
        if (targetVehicle == null) {
            throw new IllegalArgumentException("Target vehicle is null.");
        }

        if (getSaleVehicles().contains(targetVehicle))
        {
            this.getSaleVehicles().remove(targetVehicle);
        }

        if (getRentalVehicles().contains(targetVehicle))

        {
            this.getRentalVehicles().remove(targetVehicle);
        }
    }

    /**
     * Transfers a vehicle from one dealership's inventory to another.
     * </p>
     * Calls the {@link Dealership#addIncomingVehicle(Vehicle)} method
     * to add the transfer vehicle to the receving dealership.
     *
     * @param receivingDealer The {@link Dealership} receiving the vehicle.
     * @param transferVehicle The vehicle to be transferred.
     * @throws DuplicateSenderException         If the sender and receiver dealership IDs are the same.
     * @throws VehicleAlreadyExistsException    If the receiving dealership already has the vehicle in its inventory.
     * @throws DealershipNotAcceptingVehiclesException If the receiving dealership is not accepting vehicles.
     */
    public void dealershipVehicleTransfer(Dealership receivingDealer, Vehicle transferVehicle)
            throws DuplicateSenderException, VehicleAlreadyExistsException, DealershipNotAcceptingVehiclesException
    {
        if (this.equals(receivingDealer)) {
            throw new DuplicateSenderException("Sender and receiver dealership can not be the same");
        }

        this.removeVehicleFromInventory(transferVehicle);
        receivingDealer.addIncomingVehicle(transferVehicle);
    }

    /**
     * Returns a string representation of the dealership.
     * <p>
     * This method provides a concise summary of the dealership, including its ID, name,
     * and the number of vehicles in its sales and rental inventories.
     *
     * @return A string representation of the dealership.
     */
    public String toString() {
        String str = "Dealership ID: " + dealerId;
        str += "\nDealership Name: " + Objects.requireNonNullElse(name, "No name on file.");
        str += "\nSales Inventory Num: " + salesInventory.size();
        str += "\nRental Inventory Num: " + rentalInventory.size();
        return str;
    }
}