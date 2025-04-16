package javafiles.gui;

import javafiles.domainfiles.Dealership;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.Optional;

import static javafiles.gui.FXMLPath.MAIN_SCREEN;

/**
 * Controller for the profile management screen.
 * Handles actions related to managing dealership profiles, including adding dealership, editing dealership names,
 * and changing the receiving and rental statuses of dealerships.
 */
public class ProfileManagementController {
    private DealershipRow selectedDealershipRow;

    @FXML
    private TableView<DealershipRow> dealershipTable;

    @FXML
    private TableColumn<DealershipRow, String> idColumn;

    @FXML
    private TableColumn<DealershipRow, String> nameColumn;

    @FXML
    private TableColumn<DealershipRow, Boolean> receivingColumn;

    @FXML
    private TableColumn<DealershipRow, Boolean> rentingColumn;

    @FXML
    private ComboBox<String> dealerComboBox;

    @FXML
    private Label dealerNameLabel;

    /**
     * Initializes the profile management controller.
     * Sets up the table columns, fetches dealership data from AppStateManager,
     * and adds a listener to the table's selection model.
     */
    @FXML
    public void initialize() {
        // Set up the columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        receivingColumn.setCellValueFactory(new PropertyValueFactory<>("receivingEnabled"));
        rentingColumn.setCellValueFactory(new PropertyValueFactory<>("rentingEnabled"));

        // Fetch the dealership rows from AppStateManager
        ObservableList<DealershipRow> tableData = FXCollections.observableArrayList(AppStateManager.getDealershipRows());

        // Populate the ComboBox with dealership IDs and add an "All Dealerships" option
        ObservableList<String> dealershipIds = FXCollections.observableArrayList("All Dealerships");
        for (DealershipRow row : tableData) {
            dealershipIds.add(row.getId());
        }
        dealerComboBox.setItems(dealershipIds);

        // Clear the table and set the ComboBox to no selection by default
        dealershipTable.setItems(FXCollections.observableArrayList()); // Clear the table
        dealerComboBox.getSelectionModel().clearSelection(); // No default selection
        dealerNameLabel.setText(""); // Clear the label by default

        // Add a listener to update the table and label when a ComboBox selection is made
        dealerComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.equals("All Dealerships")) {
                    // Show all dealerships if "All Dealerships" is selected
                    dealershipTable.setItems(tableData);
                    dealerNameLabel.setText(""); // Clear the label
                } else {
                    // Filter the table data to show only the selected dealership
                    ObservableList<DealershipRow> filteredData = FXCollections.observableArrayList(
                        tableData.stream()
                                 .filter(row -> row.getId().equals(newValue))
                                 .toList()
                    );
                    dealershipTable.setItems(filteredData);

                    // Update the label to show the name of the selected dealership
                    if (!filteredData.isEmpty()) {
                        dealerNameLabel.setText(filteredData.get(0).getName());
                    } else {
                        dealerNameLabel.setText(""); // Clear the label if no matching dealership is found
                    }
                }
            } else {
                // Clear the table and label if no selection is made
                dealershipTable.setItems(FXCollections.observableArrayList());
                dealerNameLabel.setText("");
            }
        });

        dealershipTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedDealershipRow = newValue; // Update the selected row
        });
    }

    /**
     * Handles the "Back" button action.
     * Switches the scene to the main screen.
     *
     * @param event The ActionEvent triggered by the "Back" button.
     * @throws IOException If an I/O error occurs during scene switching.
     */
    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(MAIN_SCREEN);
    }

    /**
     * Handles the "Edit Dealership Name" button action.
     * Opens a dialog to edit the name of the selected dealership.
     *
     * @param event The ActionEvent triggered by the "Edit Dealership Name" button.
     */
    @FXML
    private void handleEditDealershipName(ActionEvent event) {
        if (selectedDealershipRow != null) {
            // Create a TextInputDialog to get the new name from the user
            TextInputDialog dialog = new TextInputDialog(selectedDealershipRow.getName());
            dialog.setTitle("Edit Dealership Name");
            dialog.setHeaderText("Edit the name of the selected dealership");
            dialog.setContentText("Enter new name:");

            // Show the dialog and capture the result
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newName -> {
                // Update the name in the selected row
                selectedDealershipRow.setName(newName);
                AppStateManager.getCompany().findDealership(selectedDealershipRow.getId()).setName(newName);

                // Refresh the table to reflect the change
                dealershipTable.refresh();
            });
        } else {
            showErrorAlert("No Dealership Selected", "Please select a dealership.");
        }
    }

    /**
     * Handles the "Add Dealership" button action.
     * Opens dialogs to add a new dealership with a unique ID and name.
     *
     * @param event The ActionEvent triggered by the "Add Dealership" button.
     */
    @FXML
    private void handleAddDealership(ActionEvent event) {

        String dealershipId = null;
        while (dealershipId == null || dealershipId.trim().isEmpty()) {
            // Prompt the user for the dealership ID
            TextInputDialog idDialog = new TextInputDialog();
            idDialog.setTitle("Add Dealership");
            idDialog.setHeaderText("Enter the ID for the new dealership");
            idDialog.setContentText("Dealership ID:");

            Optional<String> idResult = idDialog.showAndWait();
            if (idResult.isPresent()) {
                dealershipId = idResult.get().trim();

                // Check if the ID is empty
                if (dealershipId.isEmpty()) {
                    showErrorAlert("Invalid Input", "Dealership ID is required. Please enter a valid ID.");
                    dealershipId = null; // Reset to prompt again
                    continue;
                }

                // Check if the ID already exists in the table data
                final String currentDealershipId = dealershipId;
                boolean idExists = dealershipTable.getItems().stream()
                        .anyMatch(row -> row.getId().equalsIgnoreCase(currentDealershipId));
                if (idExists) {
                    String alert = "A Dealership with ID\"" + currentDealershipId + "\" already exists. \nPlease enter a unique Dealership ID.";
                    showErrorAlert("Duplicate ID", alert);
                    dealershipId = null; // Reset to prompt again
                }
            } else {
                // User canceled the dialog
                return;
            }
        }

        // Prompt the user for the dealership name
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Add Dealership");
        nameDialog.setHeaderText("Enter the name for the new dealership");
        nameDialog.setContentText("Dealership Name:");

        Optional<String> nameResult = nameDialog.showAndWait();
        final String finalDealershipId = dealershipId;
        nameResult.ifPresent(dealershipName -> {
            final String finalDealershipName = dealershipName;

            // Create a new DealershipRow and add it to the table
            DealershipRow newRow = new DealershipRow(finalDealershipId, finalDealershipName, true, false);
            dealershipTable.getItems().add(newRow);

            // Create a new Dealership object and add it to the Company object
            Dealership dealership = new Dealership(finalDealershipId, finalDealershipName);
            AppStateManager.addADealership(dealership);
        });
    }

    /**
     * Handles the "Change Receiving Status" button action, toggling the receiving status of the selected dealership.
     *
     * @author Christopher Engelhart
     */
    @FXML
    private void handleChangeReceivingStatus() {
        if (selectedDealershipRow != null) {
            Dealership dealership = AppStateManager.getCompany().findDealership(selectedDealershipRow.getId());
            if (dealership != null) {
                boolean newStatus = !selectedDealershipRow.getReceivingEnabled();
                AppStateManager.setDealershipReceivingStatus(dealership,newStatus);
                selectedDealershipRow.setReceivingEnabled(newStatus);
                dealershipTable.refresh();
            }
        } else {
            showErrorAlert("No Dealership Selected", "Please select a dealership,");
        }
    }

    /**
     * Handles the "Change Rental Status" button action, toggling the rental status of the selected dealership.
     * @author Christopher Engelhart
     */
    @FXML
    private void handleChangeRentalStatus() {
        if (selectedDealershipRow != null) {
            Dealership dealership = AppStateManager.getCompany().findDealership(selectedDealershipRow.getId());
            if (dealership != null) {
                boolean newStatus = !selectedDealershipRow.getRentingEnabled();
                AppStateManager.setDealershipRentalStatus(dealership,newStatus);
                selectedDealershipRow.setRentingEnabled(newStatus);
                dealershipTable.refresh();
            }
        } else {
            showErrorAlert("No Dealership Selected", "Please select a dealership,");
        }
    }

    /**
     * Displays an alert dialog with the given message.
     *
     * @param message The message to display in the alert.
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Inner class to represent a row in the table
    public static class DealershipRow {
        private String id;
        private String name;
        private Boolean receivingEnabled;
        private Boolean rentingEnabled;

        public DealershipRow(String id, String name, Boolean receivingEnabled, Boolean rentingEnabled) {
            this.id = id;
            this.name = name;
            this.receivingEnabled = receivingEnabled;
            this.rentingEnabled = rentingEnabled;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Boolean getReceivingEnabled() {
            return receivingEnabled;
        }

        public void setReceivingEnabled(Boolean receivingStatus) {
            this.receivingEnabled = receivingStatus;
        }

        public void setRentingEnabled(Boolean rentalStatus)
        {
            this.rentingEnabled = rentalStatus;
        }

        public Boolean getRentingEnabled() {
            return rentingEnabled;
        }
    }
}