package javafiles.gui;

import javafiles.Key;
import javafiles.customexceptions.ReadWriteException;
import javafiles.dataaccessfiles.FileIOFactory;
import javafiles.dataaccessfiles.FileIOReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static javafiles.gui.FXMLPath.INVENTORY_SCREEN;
import static javafiles.gui.FXMLPath.VEHICLE_ENTRY;


/**
 * Controller for the "Add Inventory" screen.
 * Handles actions related to adding vehicle inventory, including loading from a file and manual entry.
 */
public class AddInventoryController {


    /**
     * Handles the "Back" button action.
     * Switches the scene to the inventory screen.
     *
     * @param event The ActionEvent triggered by the "Back" button.
     * @throws IOException If an I/O error occurs during scene switching.
     */
    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(INVENTORY_SCREEN);
    }


    /**
     * Handles the "Load From File" button action.
     *
     * @param event The ActionEvent triggered by the "Load From File" button.
     * @throws IOException If an I/O error occurs during scene switching.
     */
    @FXML
    private void handleLoadFromFile(ActionEvent event) throws IOException {
        String path = FileIOFactory.selectFilePath('r');

        if (path == null) {
            return;
        }
        try {
            FileIOReader fileIOReader = FileIOFactory.buildNewFileIOReader(path);
            List<Map<Key, Object>> maps = fileIOReader.readInventory();
            List<Map<Key, Object>> badMaps = AppStateManager.dataToInventory(maps);
            GuiUtility.showMapTables(maps, badMaps);
        } catch (ReadWriteException e) {
            JOptionPane.showMessageDialog(null, "Could Not Read From File.");
        }
    }

    /**
     * Handles the "Enter Manually" button action.
     * Switches the scene to the vehicle entry screen.
     *
     * @param event The ActionEvent triggered by the "Enter Manually" button.
     * @throws IOException If an I/O error occurs during scene switching.
     */
    @FXML
    private void handleEnterManually(ActionEvent event) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(VEHICLE_ENTRY);
    }
}
