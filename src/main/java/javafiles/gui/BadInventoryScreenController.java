package javafiles.gui;

import javafiles.Key;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import javafiles.domainfiles.Dealership;
import javafiles.domainfiles.Vehicle;


import static javafiles.gui.FXMLPath.MAIN_SCREEN;

public class BadInventoryScreenController {

    @FXML
    private Button backButton;

    @FXML
    private Button discardSelectedButton;

    @FXML
    private Button uploadSelectedButton;

    @FXML
    private TableView<Vehicle> badInventoryTableView;

    @FXML
    private TableColumn<Vehicle, Boolean> selectColumn;

    @FXML
    private TableColumn<Dealership, String> dealershipIdColumn;

    @FXML
    private TableColumn<Dealership, String> dealershipNameColum;

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
    private TableColumn<Vehicle, Double> vehiclePriceColumn;

    @FXML
    private TableColumn<Vehicle, String> priceUnitColumn;

    @FXML
    private TableColumn<Vehicle, Long> acquisitionDateColumn;

    @FXML
    private TableColumn<List<Map<Key,Object>>,String> errorReasonColumn;


    @FXML
    public void initialize() {

        };



    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(MAIN_SCREEN);

    }

    @FXML
    private void handleDiscardSelected(ActionEvent event) {

        //TODO: implement discarding selected vehicles from company data
        System.out.println("Discard selected items");
    }

    @FXML
    private void handleUploadSelected(ActionEvent event) {
        //TODO: implement uploading vehicles to company
        System.out.println("Upload selected items: ");
    }

}