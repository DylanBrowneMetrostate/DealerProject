package javafiles.gui;


import javafiles.domainfiles.Company;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

import static javafiles.gui.FXMLPath.MAIN_SCREEN;

/**
 * The main application class for the Dealership application.
 * This class extends the JavaFX Application class and handles the application's lifecycle,
 * including initialization, startup, and shutdown.
 */
public class DealershipApp extends Application {
/**
     * Called when the application is stopped.
     * Writes the current inventory data to a file using {@link AppStateManager#writeToInventoryFile()}.
     */

    @Override
    public void stop() {
        AppStateManager.writeToInventoryFile();
    }


    /**
     * Called when the application is started.
     * Initializes the Company instance, loads initial data, and displays the main screen.
     *
     * @param primaryStage The primary stage for this application, onto which
     * the application scene can be set.
     * @throws Exception If an error occurs during application startup.
     */

    @Override
    public void start(Stage primaryStage) throws IOException {

        // create and intialize company instance
        Company company = new Company();

        AppStateManager.initializeCompany(company);

        SceneManager sceneManger = SceneManager.getInstance(primaryStage);
        sceneManger.switchScene(MAIN_SCREEN);
        primaryStage.show();

        AppStateManager.loadInitialFiles();


    }


    /**
     * The main method to launch the application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}