package javafiles.gui;

import javafiles.Key;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;

import java.io.IOException;
import javafiles.customexceptions.ReadWriteException;


import static javafiles.gui.FXMLPath.MAIN_SCREEN;

/**
 * Controller for the Bad Inventory Screen. This screen displays vehicles that
 * were not successfully added to the main inventory, allowing the user to edit items and
 * select items for discarding or uploading.
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
    private TableColumn<MapWithSelection, Boolean> rentalColumn;

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
    private TableColumn<MapWithSelection, String> acquisitionDateColumn;

    @FXML
    private TableColumn<MapWithSelection, String> errorReasonColumn;

    private ObservableList<MapWithSelection> badInventoryList = FXCollections.observableArrayList();

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
     * configures the "Select" column with checkboxes and calls
     * {@link #setBadInventoryData(List)} to populate the table.
     */
    @FXML
    public void initialize() {
        // Initialize the table columns
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setCellFactory(tc -> new CheckBoxTableCell<>());
        selectColumn.setEditable(true);

        // Use a Callback for dynamic mapping of Map values to TableColumns
        dealershipIdColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().getData().get(Key.DEALERSHIP_ID);
            return new SimpleStringProperty(value != null ? value.toString() : "");
        });
        dealershipNameColum.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().getData().get(Key.DEALERSHIP_NAME);
            return new SimpleStringProperty(value != null ? value.toString() : "");
        });
        vehicleIdColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().getData().get(Key.VEHICLE_ID);
            return new SimpleStringProperty(value != null ? value.toString() : "");
        });
        rentalColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().getData().get(Key.VEHICLE_RENTAL_STATUS);
            return new SimpleBooleanProperty(value != null ? (Boolean) value : false);
        }); // Default to false if null
        vehicleTypeColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().getData().get(Key.VEHICLE_TYPE);
            return new SimpleStringProperty(value != null ? value.toString() : "");
        });
        vehicleManufacturerColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().getData().get(Key.VEHICLE_MANUFACTURER);
            return new SimpleStringProperty(value != null ? value.toString() : "");
        });
        vehicleModelColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().getData().get(Key.VEHICLE_MODEL);
            return new SimpleStringProperty(value != null ? value.toString() : "");
        });
        vehiclePriceColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().getData().get(Key.VEHICLE_PRICE);
            return new SimpleStringProperty(value != null ? value.toString() : "");
        });
        priceUnitColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().getData().get(Key.VEHICLE_PRICE_UNIT);
            return new SimpleStringProperty(value != null ? value.toString() : "");
        });
        acquisitionDateColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().getData().get(Key.VEHICLE_ACQUISITION_DATE);
            return new SimpleStringProperty(value != null ? value.toString() : "");
        });
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