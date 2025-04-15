package javafiles.gui

import javafiles.dataaccessfiles.FileIOFactory
import javafiles.domainfiles.Company
import javafx.application.Application
import javafx.stage.Stage
import java.io.IOException


/**
 * The main application class for the Dealership application.
 * This class extends the JavaFX Application class and handles the application's lifecycle,
 * including initialization, startup, and shutdown.
 */
class DealershipAppTemp() : Application() {
    constructor(args: Array<String>) : this() {
        // needs to be in a method, but can't be the default constructor because Application calls that.
        // Application can not call launch(*args)
        launch(*args)
    }

    /**
     * Called when the application is stopped.
     * Writes the current inventory data to a file using [AppStateManager.writeToInventory].
     */
    override fun stop() {
        AppStateManager.writeToInventory()
    }


    /**
     * Called when the application is started.
     * Initializes the Company instance, loads initial data, and displays the main screen.
     *
     * @param primaryStage The primary stage for this application, onto which
     * the application scene can be set.
     * @throws IOException If an error occurs during application startup.
     */
    @Throws(IOException::class)
    override fun start(primaryStage: Stage) {
        // create and initialize company instance

        val company = Company()

        AppStateManager.initializeCompany(company)

        val sceneManger = SceneManager.getInstance(primaryStage)
        sceneManger.switchScene(FXMLPath.MAIN_SCREEN)
        primaryStage.show()

        AppStateManager.loadInitialFiles()
    }

}

fun main(args: Array<String>) {
    DealershipAppTemp(args)
}