package javafiles.gui;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

import static javafiles.gui.FXMLPath.*;



/**
 * Controller for the inventory management screen.
 * Handles actions related to vehicle inventory, such as adding, removing,
 * transferring, and changing the rental status of vehicles.
 * Also provides functionality to view the company's inventory.
 */
public class InventoryScreenController
{


    public Button btnChangeRental;
    public Button btnTransferVehicle;
    public Button btnRemoveVehicle;
    public Button btnAddVehicle;
    public Button btnViewInventory;

    /**
     * Handles the action when the back button is clicked.
     * Switches the scene to the Main Screen.
     *
     * @param event The ActionEvent triggered by the back button.
     * @throws IOException If an I/O error occurs during scene switching.
     */
    @FXML
    private void handleBack(ActionEvent event) throws IOException
        {
            SceneManager sceneManager = SceneManager.getInstance(null);
            sceneManager.switchScene(MAIN_SCREEN);

        }

    /**
     * Handles the action when the "Add Vehicle to Dealership" button is clicked.
     * Switches the scene to the Add Inventory screen.
     *
     * @param event The ActionEvent triggered by the "Add Vehicle to Dealership" button.
     * @throws IOException If an I/O error occurs during scene switching.
     */
    @FXML
    private void handleAddVehicleToDealership(ActionEvent event) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(ADD_INVENTORY);
    }

    /**
     * Handles the action when the "Remove Vehicle from Dealership" button is clicked.
     * Switches the scene to the Vehicle Removal screen.
     *
     * @throws IOException If an I/O error occurs during scene switching.
     */
    @FXML
    private void handleRemoveVehicleFromDealership() throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(VEHICLE_REMOVAL);
    }

    /**
     * Handles the action when the "Transfer Vehicle Between Dealerships" button is clicked.
     * Switches the scene to the Vehicle Transfer screen.
     *
     * @throws IOException If an I/O error occurs during scene switching.
     */
    @FXML
    private void handleTransferVehicleBetweenDealerships() throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(VEHICLE_TRANSFER);
    }

    /**
     * Handles the action when the "Change Vehicle Rental" button is clicked.
     * Switches the scene to the Vehicle Rental screen.
     *
     * @throws IOException If an I/O error occurs during scene switching.
     */
    @FXML
    private void handleChangeVehicleRental() throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(VEHIClE_RENTAL);
    }



    /**
     * Handles the action when the "View Company Inventory" button is clicked.
     * Switches the scene to the View Inventory screen.
     *
     * @throws IOException If an I/O error occurs during scene switching.
     */
    @FXML
    private void handleViewCompanyInventory() throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(VIEW_INVENTORY);
    }

    }



