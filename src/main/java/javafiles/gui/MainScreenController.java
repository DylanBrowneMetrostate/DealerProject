package javafiles.gui;

import javafiles.domainfiles.Company;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

import static javafiles.gui.FXMLPath.*;

/**
 * Controller for the main screen of the application.
 * Handles actions related to navigating to inventory management and company profile management screens.
 */
public class MainScreenController {

    private static Company company = new Company();

    public Company getCompany() {return company;}


    @FXML
    private Button manageCompanyInventoryButton;
    @FXML
    private Button ManageCompanyProfileButton;

    @FXML
    private  Button badInventoryManagementButton;



    @FXML
    public void initialize() {

    }

    /**
     * Handles the action when the "Manage Company Inventory" button is clicked.
     * Switches the scene to the inventory management screen.
     *
     * @param event The ActionEvent triggered by the "Manage Company Inventory" button.
     * @throws IOException If an I/O error occurs during scene switching.
     */
    @FXML
    private void handleManageCompanyInventory(ActionEvent event) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(INVENTORY_SCREEN);
    }

    /**
     * Handles the action when the "Manage Company Profile" button is clicked.
     * Switches the scene to the company profile management screen.
     *
     * @param event The ActionEvent triggered by the "Manage Company Profile" button.
     * @throws IOException If an I/O error occurs during scene switching.
     */
    @FXML
    private void handleManageCompanyProfile(ActionEvent event) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene( PROFILE_MANAGEMENT);
    }

    @FXML
    private void handleManageBadInventory() throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(BAD_INVENTORY);
    }
}
