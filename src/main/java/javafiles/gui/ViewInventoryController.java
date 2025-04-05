package javafiles.gui;

import javafiles.Key;
import javafiles.customexceptions.DealershipNotFoundException;
import javafiles.customexceptions.VehicleNotFoundException;
import javafiles.domainfiles.Dealership;
import javafiles.domainfiles.Vehicle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static javafiles.gui.FXMLPath.INVENTORY_SCREEN;

/**
 * Controller class for the View Inventory screen.
 * Handles the display of vehicle inventory in a TableView.
 */
public class ViewInventoryController {

    @FXML
    private TableView<Vehicle> tableView;

    @FXML
    private TableColumn<Vehicle, String> dealershipIdColumn;

    @FXML
    private TableColumn <Vehicle,String> dealershipNameColum;

    @FXML
    private TableColumn<Vehicle, Integer> vehicleIdColumn;
    @FXML
    private TableColumn<Vehicle, Boolean> rentalColumn;
    @FXML
    private TableColumn<Vehicle, String> vehicleTypeColumn;
    @FXML
    private TableColumn<Vehicle, String> vehicleManufacturerColumn;
    @FXML
    private TableColumn<Vehicle, String> vehicleModelColumn;
    @FXML
    private TableColumn<Vehicle, Long> vehiclePriceColumn;
    @FXML
    private TableColumn<Vehicle, String> priceUnitColumn;
    @FXML
    private TableColumn<Vehicle, Long> acquisitionDateColumn;

    /**
     * Initializes the controller. Sets up the TableView columns and loads vehicle data.
     */
    @FXML
    public void initialize() {
        // Initialize the table columns with PropertyValueFactory
        dealershipIdColumn.setCellValueFactory(cellData -> {
            String dealershipId = getDealershipIdFromMap(cellData.getValue());
            return new SimpleStringProperty(dealershipId);
        });
        dealershipNameColum.setCellValueFactory(cellData -> {
            try {
                String dealershipName = getDealershipNameFromMap(cellData.getValue());
                return new SimpleStringProperty(dealershipName);
            } catch (DealershipNotFoundException e) {
                showAlert(e.getMessage());
                return new SimpleStringProperty("Error: Dealership not found");
            }
        });


        vehicleIdColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        rentalColumn.setCellValueFactory(new PropertyValueFactory<>("rentalStatus"));
        vehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
        vehicleManufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleManufacturer"));
        vehicleModelColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleModel"));
        vehiclePriceColumn.setCellValueFactory(new PropertyValueFactory<>("vehiclePrice"));
        priceUnitColumn.setCellValueFactory(new PropertyValueFactory<>("priceUnit"));
        acquisitionDateColumn.setCellValueFactory(new PropertyValueFactory<>("formattedAcquisitionDate"));

        // Load data into the table
        loadVehicleData();
    }

    /**
     * Loads the vehicle data using {@link AppStateManager#getListCompanyVehicles()} and populates the TableView.
     */
    private void loadVehicleData() {
        ObservableList<Vehicle> vehicleList = FXCollections.observableArrayList(AppStateManager.getListCompanyVehicles());
        tableView.setItems(vehicleList);
    }


    /**
     * Retrieves the dealership ID from the map for the selected Vehicle.
     *
     * @param vehicle The Vehicle object.
     * @return The dealership ID or "" if dealership ID is not found for a vehicle.
     */
    private String getDealershipIdFromMap(Vehicle vehicle) {
        List<Map<Key, Object>> dataMaps = AppStateManager.getCompanyData();
        for (Map<Key, Object> dataMap : dataMaps) {
            if (dataMap.get(Key.VEHICLE_ID).equals(vehicle.getVehicleId())) {
                return (String) dataMap.get(Key.DEALERSHIP_ID);
            }
        }

        return "";
    }

    /**
     * Retrieves the dealership name for a vehicle.
     * <p>
     * Searches application data for the vehicle's dealership and returns the name.
     * Returns an empty string if not found.
     *
     * @param vehicle The vehicle.
     * @return The dealership name, or "" if Dealership not found
     * @throws DealershipNotFoundException if Dealership is not found in the company
     * @throws VehicleNotFoundException if vehicle does not exist in the company
     */
    private String getDealershipNameFromMap(Vehicle vehicle) throws  DealershipNotFoundException, VehicleNotFoundException {
        List<Map<Key, Object>> dataMaps = AppStateManager.getCompanyData();
        Dealership dealership;

        for (Map<Key, Object> dataMap : dataMaps) {
            // Ensure the vehicle ID is not null before comparison
            if (dataMap.get(Key.VEHICLE_ID) != null && dataMap.get(Key.VEHICLE_ID).equals(vehicle.getVehicleId())) {
                String dealerID = (String) dataMap.get(Key.DEALERSHIP_ID);
                dealership = AppStateManager.findADealership(dealerID);

                // Handle the case where findADealership returns null
                if (dealership != null) {
                    return dealership.getDealerName();
                } else {
                    throw new DealershipNotFoundException("Error: Dealership not found in company" + " vehicle " + vehicle.getVehicleId() +
                    " does not belong to a dealership"); // Dealership not found
                }
            }
        }
       throw new VehicleNotFoundException("Vehicle " + vehicle.getVehicleId() + " not found for " + vehicle.getVehicleId() + ".");
    }


        /**
         * Handles the "Back" button click event. Switches the scene to the inventory screen.
         *
         * @param event The ActionEvent triggered by the button click.
         * @throws IOException If an I/O error occurs during scene switching.
         */
        @FXML
        public void handleBack (ActionEvent event) throws IOException
        {
            SceneManager sceneManager = SceneManager.getInstance(null);
            sceneManager.switchScene(INVENTORY_SCREEN);

        }

    /**
     * Displays an alert dialog with the given message.
     *
     * @param message The message to display in the alert.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}

