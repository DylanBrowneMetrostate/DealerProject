package javafiles.gui;

import javafiles.customexceptions.DealershipNotAcceptingVehiclesException;
import javafiles.customexceptions.DuplicateSenderException;
import javafiles.customexceptions.VehicleAlreadyExistsException;
import javafiles.customexceptions.VehicleNotFoundException;
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
import java.util.ArrayList;
import java.util.List;

import static javafiles.gui.FXMLPath.INVENTORY_SCREEN;

/**
 * Controller for the Vehicle Transfer screen, handling vehicle transfer operations between dealerships.
 */
public class VehicleTransferController {

    @FXML
    private ComboBox<String> senderComboBox;

    @FXML
    private ComboBox<String> receiverComboBox;

    @FXML
    private TableView<Vehicle> vehicleTable;

    @FXML
    private TableColumn<Vehicle, String> vehicleIdColumn;

    @FXML
    private TableColumn<Vehicle, Boolean> rentalColumn;

    @FXML
    private TableColumn<Vehicle, Long> vehicleTypeColumn;

    @FXML
    private TableColumn<Vehicle, String> vehicleManufacturerColumn;

    @FXML
    private TableColumn<Vehicle, String> vehicleModelColumn;

    @FXML
    private TableColumn<Vehicle, Double> vehiclePriceColum;

    @FXML
    private TableColumn<Vehicle, String> priceUnitColumn;

    @FXML
    private TableColumn<Vehicle, String> acquisitionDateColumn;

    private Vehicle selectedVehicle;

    /**
     * Initializes the controller, populating combo boxes and setting up table columns. Combo boxes are
     * populated by {@link AppStateManager#getDealershipIDs()}.
     */
    @FXML
    public void initialize() {
        // Populate sender and receiver combo boxes with all dealerships
        List<String> dealershipIDs = AppStateManager.getDealershipIDs();
        senderComboBox.setItems(FXCollections.observableArrayList(dealershipIDs));
        receiverComboBox.setItems(FXCollections.observableArrayList(dealershipIDs));

        // Set up table columns
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

        // Add listeners to combo boxes to update the table when a new dealership is selected.
        senderComboBox.setOnAction(this::handleSenderSelection);
    }

    /**
     * Handles the "Back" button action, switching to the inventory screen.
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
     * Handles the "Transfer" button action, transferring the selected vehicle between dealerships.
     * Calls the  {@link AppStateManager#transferVehicle(String, String, Vehicle)} method to perform
     * the transfer operation.
     * @param event The action event.
     */
    @FXML
    private void handleTransferVehicle(ActionEvent event) {
        String senderDealershipId = senderComboBox.getValue();
        String receiverDealershipId = receiverComboBox.getValue();

        if (senderDealershipId == null || receiverDealershipId == null) {
            showAlert("Please select both sender and receiver dealerships.");
            return;
        }

        if (selectedVehicle == null) {
            showAlert("Please select a vehicle to transfer.");
            return;
        }

        try {
            AppStateManager.transferVehicle(senderDealershipId, receiverDealershipId, selectedVehicle);
            showAlert("Vehicle " + selectedVehicle.getVehicleId() + " transferred successfully to " + receiverDealershipId + ".");
            updateVehicleTable(senderDealershipId); // Refresh the table
        } catch ( VehicleNotFoundException | VehicleAlreadyExistsException |
                 DealershipNotAcceptingVehiclesException | DuplicateSenderException e) {
            showAlert(e.getMessage());
        }

    }

    /**
     * Handles the sender dealership selection event, updating the vehicle table and receiver combo box options.
     *
     * @param event The action event.
     */
    @FXML
    private void handleSenderSelection(ActionEvent event) {
        String senderDealershipId = senderComboBox.getValue();
        if (senderDealershipId != null) {
            updateVehicleTable(senderDealershipId);
            updateReceiverComboBoxOptions(senderDealershipId);
        }
    }

    /**
     * Updates the vehicle table with the inventory of the selected dealership.
     *
     * @param dealershipId The ID of the dealership to display vehicles from.
     */
    private void updateVehicleTable(String dealershipId) {
        ObservableList<Vehicle> vehicleData = FXCollections.observableArrayList(AppStateManager.getDealershipCompleteInventory(dealershipId));
        vehicleTable.setItems(vehicleData);
    }

    /**
     * Updates the receiver combo box options, removing the selected sender dealership.
     *
     * @param senderDealershipId The ID of the sender dealership.
     */
    private void updateReceiverComboBoxOptions(String senderDealershipId) {
        List<String> allDealershipIDs = AppStateManager.getDealershipIDs();
        List<String> receiverOptions = new ArrayList<>(allDealershipIDs);
        receiverOptions.remove(senderDealershipId); // Remove the sender's ID

        ObservableList<String> receiverObservableList = FXCollections.observableArrayList(receiverOptions);
        receiverComboBox.setItems(receiverObservableList);
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