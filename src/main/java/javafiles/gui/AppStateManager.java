package javafiles.gui;

import javafiles.Key;
import javafiles.customexceptions.*;
import javafiles.dataaccessfiles.FileIO;
import javafiles.dataaccessfiles.FileIOBuilder;
import javafiles.domainfiles.Company;
import javafiles.domainfiles.Dealership;
import javafiles.domainfiles.Vehicle;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Manages the application's state, specifically the Company instance and its data.
 * This class provides static methods to initialize, access, and modify the Company object,
 * as well as retrieve data related to the company's inventory and dealerships.
 */
public class AppStateManager {

    private static String masterInventoryList = "masterInventoryList.json";
    private static Company company;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private AppStateManager() {}

    /**
     * Initializes the Company instance with the provided Company object.
     * If the Company instance already exists, this method will not overwrite it.
     *
     * @param newCompany The Company object to initialize the application state with.
     */
    public static void initializeCompany(Company newCompany)
    {
        if (company == null)
        {
            company = newCompany;
        }
    }

    /**
     * Retrieves the current Company instance.
     *
     * @return The Company object representing the application's state.
     */
    public static Company getCompany()
    {
        return company;
    }

    /**
     * Retrieves a List of Maps representing all vehicle data within the Company instance.
     * </p>
     * Each Map contains key-value pairs representing vehicle attributes.
     * Method calls {@link Company#getDataMap()}.
     *
     * @return A List of Maps containing vehicle data.
     */
    public static List<Map<Key, Object>> getCompanyData()
    {
        return company.getDataMap();
    }

    /**
     * Retrieves a List of Dealership objects associated with the Company instance.
     * </p>
     * Method calls {@link Company#getListDealerships()}.
     *
     * @return A List of Dealership objects.
     */
    public static ArrayList<Dealership> getListDealerships()
    {
        return company.getListDealerships();
    }


    /**
     * Retrieves a comprehensive list of all vehicles owned by the company, aggregated from all dealerships.
     *
     * @return An ArrayList of Vehicle objects representing all vehicles owned by the company.
     */
    public static ArrayList<Vehicle> getListCompanyVehicles()
    {
        ArrayList<Vehicle> companyListVehicles = new ArrayList<>();

        ArrayList<Dealership> listDealerships = company.getListDealerships();

        for (Dealership dealership : listDealerships)
        {
            companyListVehicles.addAll(getDealershipCompleteInventory(dealership.getDealerId()));

        }

        return companyListVehicles;
    }

    /**
     * Adds a Dealership object to the Company instance.
     * </p>
     * Method calls {@link Company#addDealership(Dealership)}
     * 
     *
     * @param dealership The Dealership object to add to the Company.
     */
    public static void addADealership(Dealership dealership)
    {
        company.addDealership(dealership);
    }

    /**
     * Transfers a vehicle from one dealership's inventory to another.
     * </p>
     * Calls {@link Dealership#dealershipVehicleTransfer(Dealership, Vehicle)}.
     *
     * @param senderId        The ID of the dealership sending the vehicle.
     * @param receiverId      The ID of the dealership receiving the vehicle.
     * @param transferVehicle The vehicle to be transferred.
     * @throws VehicleAlreadyExistsException       If the receiving dealership already has the vehicle in its inventory.
     * @throws DealershipNotAcceptingVehiclesException If the receiving dealership is not accepting vehicles.
     * @throws DuplicateSenderException            If the sender and receiver dealership IDs are the same.
     */
    public static void transferVehicle(String senderId, String receiverId, Vehicle transferVehicle) throws
            VehicleAlreadyExistsException, DealershipNotAcceptingVehiclesException, DuplicateSenderException
    {
        Dealership sender = company.findDealership(senderId);
        Dealership receiver = company.findDealership(receiverId);
        sender.dealershipVehicleTransfer(receiver, transferVehicle);

        writeToInventory();
    }


    /**
     * Processes a list of Maps containing inventory data and adds it to the Company's inventory.
     * This method calls {@link Company#dataToInventory(List)} method.
     *
     * @param maps The list of Maps containing inventory data.
     * @return A list of Maps representing invalid data entries, if any.
     */
    public static List<Map<Key, Object>> dataToInventory(List<Map<Key, Object>> maps) {
        List<Map<Key, Object>> badMaps = company.dataToInventory(maps);
        writeToInventory();
        return badMaps;
    }

    /**
     * Loads initial inventory data from a file.
     * This method attempts to read inventory data from the specified file using FileIO,
     * and processes it using  {@link Company#dataToInventory(List)} method.
     * If a ReadWriteException occurs, it prints the error message and returns an empty list.
     */
    protected static void loadInitialFiles() {
        try {
            FileIO fileIO = FileIOBuilder.buildNewFileIO(masterInventoryList, 'r');

            List<Map<Key, Object>> maps = fileIO.readInventory();
            List<Map<Key, Object>> badDataMaps = company.dataToInventory(maps);

            if (!badDataMaps.isEmpty()) {GuiUtility.showMapTables(maps, badDataMaps);}
        } catch (ReadWriteException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    /**
     * Writes the Company's inventory data to a file.
     * </p>
     * This method retrieves the inventory data from the Company by calling
     * {@link Company#getDataMap()} and attempts to write it to the specified
     * file using FileIO. If a ReadWriteException occurs, it prints an error message.
     */
    protected static void writeToInventory() {
        List<Map<Key, Object>> data = company.getDataMap();
        try {
            FileIO fileIO = FileIOBuilder.buildNewFileIO(masterInventoryList, 'w');
            fileIO.writeInventory(data);
        } catch (ReadWriteException e) {
            JOptionPane.showMessageDialog(null, "Inventory could not be written.");
        }
    }


    /**
     * Retrieves a list of all dealership IDs from the Company.
     * This method delegates to {@link Company#getAllDealershipIds()}.
     *
     * @return An ArrayList of Strings representing all dealership IDs.
     */
    public static ArrayList<String> getDealershipIDs()
    {
        return company.getAllDealershipIds();
    }


    /**
     * Retrieves a list of dealership IDs that currently have renting enabled.
     *
     * @return A list of dealership IDs that are currently renting enabled.
     */
    public static List<String> getRentingEnabledDealershipIDs()
    {
        return company.getListDealerships().stream()
                .filter(dealership -> company.isDealershipRentingEnabled(dealership.getDealerId()))
                .map(Dealership::getDealerId)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a List of DealershipRow objects representing dealership data.
     * </p>
     * This method fetches dealership information from the Company instance using
     * {@link Company#getDealershipInfoList()} and converts it into a List of
     * {@link ProfileManagementController.DealershipRow} objects.
     *
     * @return A List of {@link ProfileManagementController.DealershipRow} objects containing
     *         dealership data such as ID, name, receiving status, and renting status.
     */
    public static List<ProfileManagementController.DealershipRow> getDealershipRows() {
        List<Map<String, Object>> dealershipInfoList = company.getDealershipInfoList();
        List<ProfileManagementController.DealershipRow> dealershipRows = new ArrayList<>();
        Set<String> existingIds = new HashSet<>();

        for (Map<String, Object> info : dealershipInfoList) {
            String id = (String) info.get("id");
            String name = (String) info.get("name");
            Boolean receivingEnabled = (Boolean) info.get("receivingEnabled");
            Boolean rentingEnabled = (Boolean) info.get("rentingEnabled");

            // Check if the ID already exists
            if (!existingIds.contains(id)) {
                dealershipRows.add(new ProfileManagementController.DealershipRow(id, name, receivingEnabled, rentingEnabled));
                existingIds.add(id);
            } 
        }
        return dealershipRows;
    }

    public static void manualVehicleAdd(Map<Key, Object> map) throws VehicleAlreadyExistsException,
            InvalidPriceException, DealershipNotAcceptingVehiclesException,
            InvalidVehicleTypeException, MissingCriticalInfoException {
        String dealershipID = Key.DEALERSHIP_ID.getVal(map, String.class);
        Dealership dealership = company.findDealership(dealershipID);
        if (dealership == null) {
            throw new IllegalArgumentException("Dealership ID not found: " + dealershipID);
        }

        company.manualVehicleAdd(map, dealership);

        writeToInventory();
    }

    /**
        Sets receiving status for a {@link Dealership} in the company.
        Method calls {@link Dealership#setReceivingVehicle(Boolean)}
     */
    public static void setDealershipReceivingStatus(Dealership dealership,boolean status)
    {
        dealership.setReceivingVehicle(status);
        writeToInventory();
    }

    /**
     Sets rental status for a {@link Dealership} in the company.
     Method calls {@link Dealership#setRentingVehicles(Boolean)}
     */
    public static void setDealershipRentalStatus(Dealership dealership,boolean status)
    {
        dealership.setRentingVehicles(status);
        writeToInventory();
    }

    /**
     * Updates the rental status of a vehicle within a dealership and moves it between
     * the dealership's sales and rental inventories based on the updated rental status.
     * </p>
     * 
     * mwethod calls {@link Company#updateVehicleRental(String, Vehicle)}
     *
    */
    public static void updateDealershipVehicleRentalState(String dealershipId, Vehicle vehicleToUpdate) throws
             RentalException

    {
        Dealership dealer = company.findDealership(dealershipId);
        dealer.updateVehicleRental(vehicleToUpdate);
        writeToInventory();
    }


    /**
     * Takes a String representing a Dealership ID and returns {@link Dealership}
     * with matching ID in the company.
     * </p>
     * Method calls {@link Company#findDealership(String)}.
     *
     * @param dealerId A String equal to the dealerID of the Dealership we are searching for.
     * @return The Dealership we are searching for in listDealerships (null if absent).
     */
    public static Dealership findADealership(String dealerId)
    {
       return company.findDealership(dealerId);
    }



    /**
     * Gets the complete inventory of a given dealership.
     * Method calls {@link Company#getDealershipCompleteInventory(String)}.
     *
     * @param dealershipId dealership ID of target dealership
     * @return ArrayList<Vehicle> represent a complete collection of target dealership's sales and rental inventory
     */
    public static ArrayList<Vehicle> getDealershipCompleteInventory(String dealershipId) {
        Dealership dealer = company.findDealership(dealershipId);
        return dealer.getTotalInventory();
    }


    /**
     * Removes target {@link Vehicle} from a {@link Dealership} inventory.
     * Method calls {@link Company#removeVehicleFromDealership(String, Vehicle)} 
     *
     * @param dealershipId target dealership to remove vehicle from
     * @param targetVehicle vehicle to be removed
     * @throws EmptyInventoryException if target dealership's inventory is empty
     * @throws IllegalArgumentException if target vehicle is null
     */
    public static void removeVehicleFromDealership(String dealershipId, Vehicle targetVehicle) throws IllegalArgumentException
    {
        Dealership dealer = company.findDealership(dealershipId);
        dealer.removeVehicleFromInventory(targetVehicle);
        writeToInventory();
    }

}
