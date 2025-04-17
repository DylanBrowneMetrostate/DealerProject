package javafiles.gui;

import javafiles.Key;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DefaultStringConverter;

import java.io.IOException;
import javafiles.customexceptions.ReadWriteException;


import static javafiles.gui.FXMLPath.MAIN_SCREEN;

/**
 * Controller for the Bad Inventory Screen. This screen displays vehicles that
 * were not successfully added to the main inventory, allowing the user to edit items and
 * select items for discarding or uploading. The "Vehicle Type" column is displayed
 * as a dropdown list for editing, and the "Acquisition Date" column uses a DatePicker.
 */
public class BadInventoryScreenController {

    @FXML
    private Button backButton;

    @FXML
    private Button discardSelectedButton;

    @FXML
    private Button uploadSelectedButton;

    @FXML
    private TableView<MapWithSelection> badInventoryTableView;

    @FXML
    private TableColumn<MapWithSelection, Boolean> selectColumn;

    @FXML
    private TableColumn<MapWithSelection, String> dealershipIdColumn;

    @FXML
    private TableColumn<MapWithSelection, String> dealershipNameColum;

    @FXML
    private TableColumn<MapWithSelection, String> vehicleIdColumn;

    @FXML
    private TableColumn<MapWithSelection, String> vehicleTypeColumn;

    @FXML
    private TableColumn<MapWithSelection, String> vehicleManufacturerColumn;

    @FXML
    private TableColumn<MapWithSelection, String> vehicleModelColumn;

    @FXML
    private TableColumn<MapWithSelection, String> vehiclePriceColumn;

    @FXML
    private TableColumn<MapWithSelection, String> priceUnitColumn;

    @FXML
    private TableColumn<MapWithSelection, LocalDate> acquisitionDateColumn;

    @FXML
    private TableColumn<MapWithSelection, String> errorReasonColumn;

    private ObservableList<MapWithSelection> badInventoryList = FXCollections.observableArrayList();
    private final ObservableList<String> vehicleTypeOptions = FXCollections.observableArrayList("SUV", "Sedan", "Sports car", "Pickup");

    private List<Map<Key, Object>> badInventoryData;

    /**
     * Sets the bad inventory data to be displayed in the table. This data is
     * retrieved from the {@link AppStateManager}.
     *
     * @param data The list of maps containing the bad inventory data.
     */
    public void setBadInventoryData(List<Map<Key, Object>> data) {
        this.badInventoryData = AppStateManager.getBadDataInventory();
        populateTableView();
    }

    /**
     * Initializes the controller after the FXML file has been loaded. It sets up
     * the cell value factories for each column in the table view to display
     * the appropriate data from the {@link MapWithSelection} objects. It also
     * configures editable behavior for all columns except the "Error Reason" column.
     * The "Vehicle Type" column is configured to use a dropdown list for editing,
     * and the "Acquisition Date" column uses a DatePicker for selection.
     * Finally, it calls {@link #setBadInventoryData(List)} to populate the table.
     */
    @FXML
    public void initialize() {

        badInventoryTableView.setEditable(true);

        // Initialize the "Select" column with checkboxes (already editable)
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setCellFactory(tc -> new CheckBoxTableCell<>());

        // Make other String columns editable with TextFieldTableCell
        dealershipIdColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().getData().get(Key.DEALERSHIP_ID);
            return new SimpleStringProperty(value != null ? value.toString() : "");
        });
        dealershipIdColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        dealershipIdColumn.setOnEditCommit(event -> event.getRowValue().getData().put(Key.DEALERSHIP_ID, event.getNewValue()));

        dealershipNameColum.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().getData().get(Key.DEALERSHIP_NAME);
            return new SimpleStringProperty(value != null ? value.toString() : "");
        });
        dealershipNameColum.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        dealershipNameColum.setOnEditCommit(event -> event.getRowValue().getData().put(Key.DEALERSHIP_NAME, event.getNewValue()));

        vehicleIdColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().getData().get(Key.VEHICLE_ID);
            return new SimpleStringProperty(value != null ? value.toString() : "");
        });
        vehicleIdColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        vehicleIdColumn.setOnEditCommit(event -> event.getRowValue().getData().put(Key.VEHICLE_ID, event.getNewValue()));

        // Make Vehicle Type column editable with ComboBoxTableCell
        vehicleTypeColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().getData().get(Key.VEHICLE_TYPE);
            return new SimpleStringProperty(value != null ? value.toString() : "");
        });
        vehicleTypeColumn.setCellFactory(javafx.scene.control.cell.ComboBoxTableCell.forTableColumn(vehicleTypeOptions));
        vehicleTypeColumn.setOnEditCommit(event -> event.getRowValue().getData().put(Key.VEHICLE_TYPE, event.getNewValue()));

        vehicleManufacturerColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().getData().get(Key.VEHICLE_MANUFACTURER);
            return new SimpleStringProperty(value != null ? value.toString() : "");
        });
        vehicleManufacturerColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        vehicleManufacturerColumn.setOnEditCommit(event -> event.getRowValue().getData().put(Key.VEHICLE_MANUFACTURER, event.getNewValue()));

        vehicleModelColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().getData().get(Key.VEHICLE_MODEL);
            return new SimpleStringProperty(value != null ? value.toString() : "");
        });
        vehicleModelColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        vehicleModelColumn.setOnEditCommit(event -> event.getRowValue().getData().put(Key.VEHICLE_MODEL, event.getNewValue()));

        vehiclePriceColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().getData().get(Key.VEHICLE_PRICE);
            return new SimpleStringProperty(value != null ? value.toString() : "");
        });
        vehiclePriceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        vehiclePriceColumn.setOnEditCommit(event -> {
            try {
                Long newPrice = Long.parseLong(event.getNewValue());
                event.getRowValue().getData().put(Key.VEHICLE_PRICE, newPrice);
            } catch (NumberFormatException e) {
                showAlert("Invalid Price Format. Please enter a number.");
                badInventoryTableView.refresh(); // Revert to the old value
            }
        });

        priceUnitColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().getData().get(Key.VEHICLE_PRICE_UNIT);
            return new SimpleStringProperty(value != null ? value.toString() : "");
        });
        priceUnitColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        priceUnitColumn.setOnEditCommit(event -> event.getRowValue().getData().put(Key.VEHICLE_PRICE_UNIT, event.getNewValue()));

        // Make Acquisition Date column editable with DatePicker
        acquisitionDateColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().getData().get(Key.VEHICLE_ACQUISITION_DATE);
            Instant instant = value instanceof Long ? Instant.ofEpochMilli((Long) value) : null;
            LocalDate localDate = instant != null ? instant.atZone(ZoneId.systemDefault()).toLocalDate() : null;
            return new SimpleObjectProperty<>(localDate);
        });

        // get the American MM-DD-YYYY date format
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        acquisitionDateColumn.setCellFactory(column -> new TableCell<MapWithSelection, LocalDate>() {
            private final DatePicker datePicker = new DatePicker();
            private MapWithSelection currentRowData; // To store the MapWithSelection for the current row

            {
                // Event handler for when a date is selected in the DatePicker
                datePicker.setOnAction(event -> {
                    if (isEditing()) {
                        LocalDate selectedDate = datePicker.getValue();
                        if (selectedDate != null) {
                            long epochMillis = selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
                            commitEdit(selectedDate);
                            if (currentRowData != null) {
                                // Update the underlying map with Epoch Long
                                currentRowData.getData().put(Key.VEHICLE_ACQUISITION_DATE, epochMillis);
                                badInventoryTableView.refresh();
                            }
                        }
                    }
                });
                //  // Event handler for when the DatePicker loses focus
                datePicker.focusedProperty().addListener((obs, oldFocus, newFocus) -> {
                    if (!newFocus && isEditing() && datePicker.getValue() != null) {
                        LocalDate selectedDate = datePicker.getValue();
                        long epochMillis = selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
                        commitEdit(selectedDate); // Indicate that the edit is complete
                        if (currentRowData != null) {
                            currentRowData.getData().put(Key.VEHICLE_ACQUISITION_DATE, epochMillis);
                            badInventoryTableView.refresh();
                        }
                    }
                });
            }

            /**
             * Updates the content of the cell. If the cell is empty, it sets the text and graphic to null.
             * Otherwise, it formats the {@link LocalDate} item into MM-DD-YYYY formatted string for display
             * and ensures no graphic is set when not in editing mode.
             *
             * @param item  The new item for this cell.
             * @param empty Whether this cell represents data from an actual row/item.
             */
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                this.currentRowData = empty ? null : getTableView().getItems().get(getIndex()); // Get the MapWithSelection for the current row

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(dateFormatter.format(item)); // Format the LocalDate to MM-dd-yyyy
                    setGraphic(null); // Ensure no graphic DatePicker is displayed when not editing
                }
            }

            /**
             * Starts the editing process for this cell. It checks if a graphic (DatePicker) is already
             * present. If not, it retrieves the current {@link LocalDate} from the cell, sets it as the
             * value of the DatePicker, displays the DatePicker as the cell's graphic, and clears the cell's text.
             */
            @Override
            public void startEdit() {
                super.startEdit();
                if (getGraphic() == null) {
                    LocalDate currentDate = getItem();
                    datePicker.setValue(currentDate);
                    setGraphic(datePicker);
                    setText(null);
                }
            }

            /**
             * Cancels the editing process for this cell. It restores the cell's text to the formatted
             * {@link LocalDate} (if not null) and removes the DatePicker graphic.
             */
            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem() != null ? dateFormatter.format(getItem()) : null); // restore formatted date
                setGraphic(null);
            }
        });

        // Error Reason Column - Not Editable
        errorReasonColumn.setCellValueFactory(cellData -> {
            Object errorObject = cellData.getValue().getData().get(Key.REASON_FOR_ERROR);
            String reasonText;

            if (errorObject instanceof ReadWriteException) {
                reasonText = GuiUtility.CauseEnum.getCauseKey((ReadWriteException) errorObject);
            } else if (errorObject != null) {
                reasonText = errorObject.toString(); // Fallback if it's not a ReadWriteException
            } else {
                reasonText = ""; // default value if null
            }
            return new SimpleStringProperty(reasonText);
        });

        // Load and display the initial bad inventory data
        setBadInventoryData(AppStateManager.getBadDataInventory());

    }

    /**
     * Populates the table view with data from the {@link #badInventoryData} list.
     * It clears any existing items in the {@link #badInventoryList}, creates
     * {@link MapWithSelection} wrappers for each data map, and sets the
     * {@link #badInventoryList} as the items for the {@link #badInventoryTableView}.
     */
    private void populateTableView() {
        if (badInventoryData != null) {
            badInventoryList.clear();
            for (Map<Key, Object> dataMap : badInventoryData) {
                badInventoryList.add(new MapWithSelection(dataMap));
            }
            badInventoryTableView.setItems(badInventoryList);
        }
    }

    /**
     * Handles the action when the "Back" button is clicked. It navigates the user
     * back to the main screen.
     *
     * @param event The action event triggered by the button click.
     * @throws IOException If there is an error loading the main screen FXML.
     */
    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(MAIN_SCREEN);
    }

    @FXML
    private void handleDiscardSelected(ActionEvent event) {
        System.out.println("Discarding selected items: ");
        // TODO: Implement logic to discard these items
    }

    @FXML
    private void handleUploadSelected(ActionEvent event) {
        System.out.println("Uploading selected items: ");
        // TODO: Implement logic to upload these items
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
     * Helper class to wrap a {@code Map<Key, Object>} with a selection property.
     * This allows the table view to track which rows have been selected via
     * the checkbox in the "Select" column.
     */
    public static class MapWithSelection {

        private final Map<Key, Object> data;
        private final BooleanProperty selected = new SimpleBooleanProperty(false);

        /**
         * Constructs a new {@code MapWithSelection} object.
         *
         * @param data The {@code Map<Key, Object>} to wrap.
         */
        public MapWithSelection(Map<Key, Object> data) {
            this.data = data;
        }

        /**
         * Returns the underlying data map.
         *
         * @return The {@code Map<Key, Object>} held by this wrapper.
         */
        public Map<Key, Object> getData() {
            return data;
        }

        /**
         * Returns the selection state of this item.
         *
         * @return true if the item is selected, false otherwise.
         */
        public boolean isSelected() {
            return selected.get();
        }

        /**
         * Returns the boolean property representing the selection state. This is
         * used by the {@link TableView} to manage the checkbox.
         *
         * @return The {@code BooleanProperty} for the selection state.
         */
        public BooleanProperty selectedProperty() {
            return selected;
        }

        /**
         * Sets the selection state of this item.
         *
         * @param selected The new selection state.
         */
        public void setSelected(boolean selected) {
            this.selected.set(selected);
        }
    }
}