package javafiles.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * The SceneManager class is responsible for managing the scenes of the application.
 * It follows the Singleton design pattern to ensure that only one instance of SceneManager exists
 * for a given stage.
 */
public class SceneManager {

    /** The stage for the application window. */
    private Stage stage;


    /** The singleton instance of SceneManager. */
    private static SceneManager instance;

    /**
     * Private constructor to initialize the SceneManager with the given stage.
     * This constructor is private to ensure only one instance of SceneManager exists.
     *
     * @param stage the main stage (window) of the application
     */
    private SceneManager(Stage stage) {
        this.stage = stage;
    }

    /**
     * Returns the singleton instance of SceneManager.
     * If an instance does not exist, it creates one using the provided stage.
     *
     * @param stage the main stage (window) of the application
     * @return the singleton instance of SceneManager
     */
    public static SceneManager getInstance(Stage stage) {
        if (instance == null) {
            instance = new SceneManager(stage);
        }
        return instance;
    }

    /**
     * Loads a scene from the specified FXML file and stores it in the scenes map.
     * The scene is also set with a title based on the FXML file's name.
     *
     * @param fxmlPath the path to the FXML file to load the scene from
     * @throws IOException if there is an error loading the FXML file
     */
    public void switchScene(FXMLPath fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath.getPath()));
        Parent root = loader.load();
        Scene scene = new Scene(root,600,400);
        stage.setScene(scene);
        stage.setTitle(fxmlPath.getScreenTitle());
    }

}
