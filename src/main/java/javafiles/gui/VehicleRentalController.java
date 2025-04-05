package javafiles.gui;

import javafiles.customexceptions.RentalException;
import javafiles.domainfiles.Dealership;
import javafiles.domainfiles.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;

import static javafiles.gui.FXMLPath.INVENTORY_SCREEN;


/**
 * Controller for the Vehicle Rental screen, allowing users to change rental status of vehicle
 * to true or false.
 */
public class VehicleRentalController {

    @FXML
    private ComboBox<String> dealershipComboBox;

    @FXML
    private TableView<Vehicle> vehicleTable;

    @FXML
    private TableColumn<Vehicle, String> vehicleIdColumn;

    @FXML
    private TableColumn<Vehicle, Long> vehicleTypeColumn;

    @FXML
    private TableColumn<Vehicle, String> vehicleManufacturerColumn;

    @FXML
    private TableColumn<Vehicle, String> vehicleModelColumn;

    @FXML
    private TableColumn<Vehicle, Boolean> rentalColumn;

    @FXML
    private TableColumn<Vehicle, Long> acquisitionDateColumn;

    private Vehicle selectedVehicle;

    /**
     * Initializes the controller, setting up the combo box and table. Combo box is populated by calling the method
     * {@link AppStateManager#getRentingEnabledDealershipIDs()}.
     */
    @FXML
    public void initialize() {
        // Populate the combo box with dealerships that have renting enabled
        List<String> rentingEnabledDealershipIDs = AppStateManager.getRentingEnabledDealershipIDs();
        dealershipComboBox.setItems(FXCollections.observableArrayList(rentingEnabledDealershipIDs));

        // Set up the table columns
        vehicleIdColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        vehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
        rentalColumn.setCellValueFactory(new PropertyValueFactory<>("rentalStatus"));
        vehicleModelColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleModel"));
        vehicleManufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleManufacturer"));
        acquisitionDateColumn.setCellValueFactory(new PropertyValueFactory<>("formattedAcquisitionDate"));

        // Add a listener to the selected item in the table
        vehicleTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedVehicle = newValue;
            }
        });

        //Add a listener to the combo box to update the table when a new dealership is selected.
        dealershipComboBox.setOnAction(this::handleDealershipSelection);
    }

    /**
     * Handles the "Back" button action, switching to the main screen.
     *
     * @param event The action event.
     * @throws IOException If an I/O error occurs.
     */
    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(INVENTORY_SCREEN);
    }

    /**
     * Handles the "Change Rental" button action, toggling the rental status of the selected vehicle.
     *
     * @param event The action event.
     */
    @FXML
    private void handleChangeRental(ActionEvent event) {
        String selectedDealershipId = dealershipComboBox.getValue();
        if (selectedDealershipId == null) {
            showAlert("Please select a dealership.");
            return;
        }

        if (selectedVehicle == null) {
            showAlert("Please select a vehicle.");
            return;
        }

        Dealership dealership = AppStateManager.findADealership(selectedDealershipId);
        if (dealership == null) {
            showAlert("Dealership not found.");
            return;
        }

        //selectedVehicle.setRentable(!selectedVehicle.isRentable());
        vehicleTable.refresh();

        // Update the vehicle in the dealership's inventory
        try {
            AppStateManager.updateDealershipVehicleRentalState(selectedDealershipId, selectedVehicle);
        } catch (RentalException e) {
            showAlert(e.getMessage());
        }

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

    /**
     * Handles the dealership selection event, updating the vehicle table with the selected dealership's vehicles.
     *
     * @param event The action event.
     */
    @FXML
    private void handleDealershipSelection(ActionEvent event) {
      String selectedDealershipId = dealershipComboBox.getValue();
        if (selectedDealershipId != null) {
            Dealership dealership = AppStateManager.findADealership(selectedDealershipId);
            if (dealership != null) {
                ObservableList<Vehicle> vehicleData = FXCollections.observableArrayList(AppStateManager.getDealershipCompleteInventory(selectedDealershipId));
                vehicleTable.setItems(vehicleData);
            }
        }
    }
}