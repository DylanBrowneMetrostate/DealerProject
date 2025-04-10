package javafiles.domainfiles;

import javafiles.Key;
import javafiles.customexceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a central manager for all dealerships within a vehicle dealership system.
 * <p>
 * This class maintains a list of Dealership objects and provides functionality to add and retrieve dealerships by ID or name.
 * It supports operations for toggling rental status for vehicles, enabling or disabling vehicle receiving
 * at dealerships, and transferring vehicles between rental and sales inventories. The class also handles
 * data import by mapping structured input into dealership inventories.
 * </p>
 */
public class Company {
    private final ArrayList<Dealership> listDealerships;

    public Company() {
        this.listDealerships = new ArrayList<>();
    }

    public void addDealership(Dealership dealership) {listDealerships.add(dealership);}

    public ArrayList<Dealership> getListDealerships() {return listDealerships;}

    /**
     * Takes a String representing a Dealership ID and returns the index of that
     * Dealership in this Company's listDealerships.
     *
     * @param dealerId A String equal to the dealerId of the Dealership we are searching for.
     * @return The index of the searched for Dealership in listDealerships (-1 if absent).
     */
    public int getDealershipIndex(String dealerId) {
        Dealership dealership;
        for (int i = 0; i < listDealerships.size(); i++) {
            dealership = listDealerships.get(i);
            if (dealership.getDealerId().equals(dealerId)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Takes a String representing a Dealership ID and returns the given
     * Dealership in this Company's listDealerships.
     *
     * @param dealerId A String equal to the dealerID of the target Dealership.
     * @return The Dealership target dealership (null if absent).
     */
    public Dealership findDealership(String dealerId) {
        for (Dealership dealership : listDealerships) {
            if (dealership.getDealerId().equals(dealerId)) {
                return dealership;
            }
        }
        return null;
    }

    /**
     * Checks if a dealership with the given ID has renting services enabled.
     *
     * @param dealershipId The ID of the dealership to check.
     * @return true if the dealership has renting enabled, false otherwise.
     */
    public boolean isDealershipRentingEnabled(String dealershipId){
        Dealership dealership = findDealership(dealershipId);
        if (dealership != null) {
            return dealership.getRentingVehicles();
        }
        return false; // Dealership not found, or renting is disabled.
    }

    /**
     * Adds a new vehicle to the dealership inventory based on the provided vehicle details.
     * This method creates a new vehicle based on the vehicle type and sets its attributes
     * using the provided parameters in map.
     *</p>
     * {@link VehicleFactory#createVehicle(String, String, String, Long)} is used to create and validate the vehicle type.
     * If the vehicle type is unsupported, the method will throw an exception and return without
     * making any changes to the inventory. If the vehicle is created successfully, it will be added
     * to the dealership's inventory.
     *
     * @param map A {@link Map} containing all attributes of the Vehicle to be created.
     * @param dealer The {@link Dealership} that will receive the Vehicle.
     * @throws DealershipNotAcceptingVehiclesException If the dealership is not currently accepting new vehicles.
     * @throws VehicleAlreadyExistsException If the vehicle is already present in either the sales or rental inventory.
     * @throws InvalidVehicleTypeException If the vehicle type is not supported.
     * @throws InvalidPriceException if the vehicle price is not a positive value
     */
    public void manualVehicleAdd(Map<Key, Object> map, Dealership dealer) throws InvalidVehicleTypeException,
            VehicleAlreadyExistsException, DealershipNotAcceptingVehiclesException,
            InvalidPriceException, MissingCriticalInfoException {
        String id = Key.VEHICLE_ID.getVal(map, String.class);
        String make = Key.VEHICLE_MANUFACTURER.getVal(map, String.class);
        String model = Key.VEHICLE_MODEL.getVal(map, String.class);
        Long price = Key.VEHICLE_PRICE.getVal(map, Long.class);
        Long acqDate = Key.VEHICLE_ACQUISITION_DATE.getVal(map, Long.class);
        String type = Key.VEHICLE_TYPE.getVal(map, String.class);
        String unit = Key.VEHICLE_PRICE_UNIT.getVal(map, String.class);
        if (isVehicleInInventoryById(id)) {
            throw new VehicleAlreadyExistsException("This vehicle is already located in the inventory. " +
                    "Vehicle ID: " + id + " was not added to dealership " + dealer.getDealerId() + ".");
        }
        dealer.manualVehicleAdd(id, make, model, price, acqDate, type, unit);
    }

    /**
     * Returns weather a given Vehicle ID is in any Dealership of the Company.
     *
     * @param id The id of the Vehicle searched for.
     * @return weather the Vehicle is in the company.
     */
    private boolean isVehicleInInventoryById(String id){
        for (Dealership dealership : listDealerships) {
            if (dealership.isVehicleInInventoryById(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Takes a List of Map<Key, Object>s representing a List of Vehicle information
     * and writes the data in each map to the corresponding Dealership.
     *
     * @param data The List of Maps containing Vehicle information to be added to inventory.
     */
    public List<Map<Key, Object>> dataToInventory(List<Map<Key, Object>> data) {
        List<Map<Key, Object>> badInventoryMaps = new ArrayList<>();

        if (data == null) {return badInventoryMaps;}

        // Used to ensure that the cars for new Dealerships are
        // added before considering rental or receiving statuses.
        // Assumes that all Vehicles from the same dealership have
        // the same rental and receiving statuses.
        Map<Dealership, Map<Key, Object>> newDealershipStat = new HashMap<>();

        for (Map<Key, Object> map: data) {
            if (map.containsKey(Key.REASON_FOR_ERROR)) {
                badInventoryMaps.add(map);
                continue;
            }

            String id = Key.DEALERSHIP_ID.getVal(map, String.class);
            String name = Key.DEALERSHIP_NAME.getVal(map, String.class);

            if (id == null) {
                MissingCriticalInfoException cause = new MissingCriticalInfoException("No dealerID.");
                ReadWriteException exception = new ReadWriteException(cause);
                Key.REASON_FOR_ERROR.putValid(map, exception);
                badInventoryMaps.add(map);
                continue;
            }

            String v_id = Key.VEHICLE_ID.getVal(map, String.class);
            if (v_id != null) {
                if (isVehicleInInventoryById(v_id)) {
                    VehicleAlreadyExistsException cause = new VehicleAlreadyExistsException("Duplicate Vehicle ID in inventory");
                    ReadWriteException exception = new ReadWriteException(cause);
                    Key.REASON_FOR_ERROR.putValid(map, exception);
                    badInventoryMaps.add(map);
                    continue;
                }
            }

            Dealership dealership = findDealership(id);
            if (dealership == null) {
                dealership = new Dealership(id, name);
                addDealership(dealership);

                newDealershipStat.put(dealership, map);
            }

            if ( !dealership.dataToInventory(map) ) {
                badInventoryMaps.add(map);
            }
        }

        for (Dealership dealership : newDealershipStat.keySet()) {
            Map<Key, Object> map = newDealershipStat.get(dealership);
            dealership.setReceivingVehicle(Key.DEALERSHIP_RECEIVING_STATUS.getVal(map, Boolean.class));
            dealership.setRentingVehicles(Key.DEALERSHIP_RENTING_STATUS.getVal(map, Boolean.class));
        }

        return badInventoryMaps;
    }

    /**
     * Retrieves Vehicle data for all Dealerships within the Company.
     * <p>
     * This method gathers Vehicle information from all Dealerships associated with the
     * Company and compiles it into a single list of Maps. Each Map in the List
     * represents a Vehicle and contains its attributes.
     *
     * @return A {@link List} of {@link Map} Objects. Each {@link Map} represents a Vehicle
     *         and contains its attributes (dealership ID, vehicle type, manufacturer, model,
     *         vehicle ID, price, and acquisition date) as key-value pairs. Returns all vehicles from each
     *         Dealership in the Company. Returns an empty list if the Company has no Dealerships
     *         or if none of the Dealerships have any Vehicles.
     */
    public List<Map<Key, Object>> getDataMap() {
        List<Map<Key, Object>> list = new ArrayList<>();
        for (Dealership dealership : listDealerships) {
            list.addAll(dealership.getDataMap());
        }
        return list;
    }

    /**
     * Generates a formatted list of Dealership IDs.
     * <p>
     * This method retrieves all Dealerships associated with the Company and
     * creates a String containing their IDs, separated by tabs. The IDs are arranged
     * with a maximum of 6 IDs per line. If the Company has no Dealerships,
     * the method returns a message indicating this.
     *
     * @return A string containing the formatted list of dealership IDs, or the
     *         message "No valid Dealerships." if the company has no dealerships.
     */
    public String getDealershipIdList() {
        StringBuilder output = new StringBuilder();
        int added = 0;
        int idPerLine = 6;
        for (Dealership dealership : listDealerships) {
            output.append(dealership.getDealerId()).append("\t");
            if (added % idPerLine == idPerLine - 1) {
                output.append("\n");
            }
            added++;
        }
        if (output.isEmpty()) {
            return "No valid Dealerships.";
        }
        return output.toString();
    }

    /**
     * Returns an ArrayList of Strings representing all Dealership IDs in the company.
     *
     * @return An ArrayList of Strings containing all Dealership IDs.
     */
    public ArrayList<String> getAllDealershipIds() {
        ArrayList<String> dealershipIds = new ArrayList<>();
        for (Dealership dealership : listDealerships) {
            dealershipIds.add(dealership.getDealerId());
        }
        return dealershipIds;
    }

    /**
     * Returns a List of Maps containing data about all Dealerships in the Company.
     *
     * @return the List of Maps containing Dealership info.
     */
    public List<Map<String, Object>> getDealershipInfoList() {
        List<Map<String, Object>> dealershipInfoList = new ArrayList<>();
        for (Dealership dealership : listDealerships) {
            Map<String, Object> dealershipInfo = new HashMap<>();
            dealershipInfo.put("id", dealership.getDealerId());
            dealershipInfo.put("name", dealership.getDealerName());
            dealershipInfo.put("receivingEnabled", dealership.getStatusAcquiringVehicle());
            dealershipInfo.put("rentingEnabled", dealership.getRentingVehicles());
            dealershipInfoList.add(dealershipInfo);
        }
        return dealershipInfoList;
    }

    /**
     * Updates the Dealership receiving status for Vehicles and prints the appropriate
     * message for that update based on what the userInput read from the user.
     *
     * @param dealerIndex The index of the Dealership to be updated in listDealerships.
     * @param userInput The input provided by the user that is being processed.
     * @return Whether the input is invalid, true if it is invalid, false otherwise.
     */
    public boolean changeReceivingStatus(int dealerIndex, String userInput) {
        Dealership dealer = listDealerships.get(dealerIndex);
        if (userInput.equalsIgnoreCase("enable")) {
            // Check if the dealership's vehicle receiving status is already enabled
            if (dealer.getStatusAcquiringVehicle()) {
                System.out.println("Dealership " + dealer.getDealerId() + " is already set to receive vehicles.");
            } else {
                // Enable vehicle receiving for the dealership
                dealer.setReceivingVehicle(true);
                System.out.println("Vehicle receiving status for dealership " + dealer.getDealerId() + " has been enabled.");
            }
            return false;
        } else if (userInput.equalsIgnoreCase("disable")) {
            // Disable the vehicle receiving status
            if (!dealer.getStatusAcquiringVehicle()) {
                System.out.println("Dealership " + dealer.getDealerId() + " is already set to not receive vehicles.");
            } else {
                dealer.setReceivingVehicle(false);
                System.out.println("Vehicle receiving status for dealership " + dealer.getDealerId() + " has been disabled.");
            }
            return false;
        }
        System.out.println("Invalid input. Please enter 'enable' or 'disable'.");
        return true;
    }
}