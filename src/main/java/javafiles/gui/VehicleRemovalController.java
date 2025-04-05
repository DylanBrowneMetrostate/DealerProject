package javafiles.gui;


import javafiles.domainfiles.Dealership;
import javafiles.domainfiles.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;

import static javafiles.gui.FXMLPath.INVENTORY_SCREEN;


/**
 * Controller for the Vehicle Removal screen. The screen has a dropdown where a dealership is selected,
 * and once dealership is selected the table populates with that dealership's vehicles. Once vehicle is chosen
 * it can be removed from inventory by clicking the removal button.
 */
public class VehicleRemovalController {

        @FXML
        private Button backButton;

        @FXML
        private ComboBox<String> dealershipComboBox;

        @FXML
        private Button removeVehicleButton;

        @FXML
        private TableView<Vehicle> vehicleTable;

        @FXML
        private TableColumn<Vehicle, String> vehicleIdColumn;

        @FXML
        private TableColumn<Vehicle, Boolean> rentalColumn;

        @FXML
        private TableColumn<Vehicle, String> vehicleTypeColumn;

        @FXML
        private TableColumn<Vehicle, String> vehicleManufacturerColumn;

        @FXML
        private TableColumn<Vehicle, String> vehicleModelColumn;

        @FXML
        private TableColumn<Vehicle, Long> vehiclePriceColum;

        @FXML
        private TableColumn<Vehicle, Long> priceUnitColumn;

        @FXML
        private TableColumn<Vehicle, Long> acquisitionDateColumn;

        private Vehicle selectedVehicle;

    /**
     * Initializes the controller, populating the dealership combo box with all dealership IDs in
     *  the company, and setting up the vehicle table.
     */
        @FXML
        public void initialize() {

            List<String> listOfAllDealershipIds = AppStateManager.getDealershipIDs();
            dealershipComboBox.setItems(FXCollections.observableArrayList(listOfAllDealershipIds));

            vehicleIdColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
            rentalColumn.setCellValueFactory(new PropertyValueFactory<>("rentalStatus"));
            vehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
            vehicleManufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleManufacturer"));
            vehicleModelColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleModel"));
            vehiclePriceColum.setCellValueFactory(new PropertyValueFactory<>("vehiclePrice"));
            priceUnitColumn.setCellValueFactory(new PropertyValueFactory<>("priceUnit"));
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
     * Handles the event when the "Back" button is clicked, navigating back to the inventory screen.
     *
     * @param event The ActionEvent triggered by the button click.
     * @throws IOException If an I/O error occurs during scene switching.
     */
        @FXML
        public void handleBack(ActionEvent event) throws IOException {
            SceneManager sceneManager = SceneManager.getInstance(null);
            sceneManager.switchScene(INVENTORY_SCREEN);
        }

    /**
     * Handles the event when the "Remove Vehicle" button is clicked.
     * Removes the selected vehicle from the dealership and refreshes the vehicle table.
     *
     * @param event The ActionEvent triggered by the button click.
     * @throws IllegalArgumentException If an error occurs during the vehicle removal process.
     */
    @FXML
        public void handleRemoveVehicle(ActionEvent event) throws IllegalArgumentException {
            Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();

            if (selectedVehicle == null) {
                showAlert("Please select a vehicle to remove.");
                return; // Exit the method if no vehicle is selected
            }

            try {
                AppStateManager.removeVehicleFromDealership(dealershipComboBox.getValue(), selectedVehicle);
                handleDealershipSelection(event); // Reload the table data
            } catch (IllegalArgumentException e) {
                showAlert(e.getMessage());
            }
        }

        /**
         * Handles the dealership selection event, updating the vehicle table with the selected dealership's vehicles.
         *
         * @param event The action event.
         */
        @FXML
        public void handleDealershipSelection(ActionEvent event) {
            String selectedDealershipId = dealershipComboBox.getValue();
            if (selectedDealershipId != null) {
                Dealership dealership = AppStateManager.findADealership(selectedDealershipId);
                if (dealership != null) {
                    ObservableList<Vehicle> vehicleData = FXCollections.observableArrayList(AppStateManager.getDealershipCompleteInventory(selectedDealershipId));
                    vehicleTable.setItems(vehicleData);
                }
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
}
