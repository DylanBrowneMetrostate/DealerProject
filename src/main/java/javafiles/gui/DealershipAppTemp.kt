package javafiles.gui

import dealerproject.HelloApplication
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

/*
import javafiles.dataaccessfiles.FileIOBuilder;
import javafiles.domainfiles.Company; */

/**
 * The main application class for the Dealership application.
 * This class extends the JavaFX Application class and handles the application's lifecycle,
 * including initialization, startup, and shutdown.
 */
class DealershipAppTemp : Application() {
    /**
     * Called when the application is stopped.
     * Writes the current inventory data to a file using [AppStateManager.writeToInventory].
     */
    /*

    @Override
    public void stop() {
        AppStateManager.writeToInventory();
    }

    */
    /**
     * Called when the application is started.
     * Initializes the Company instance, loads initial data, and displays the main screen.
     *
     * @param primaryStage The primary stage for this application, onto which
     * the application scene can be set.
     * @throws Exception If an error occurs during application startup.
     */
    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("/MainScreen.fxml"))
        val scene = Scene(fxmlLoader.load(), 320.0, 240.0)
        primaryStage.title = "Hello!"
        primaryStage.scene = scene
        primaryStage.show()
    }
}
fun main(args: Array<String>) {
    println("Hello World!")
    Application.launch(DealershipAppTemp::class.java);
}