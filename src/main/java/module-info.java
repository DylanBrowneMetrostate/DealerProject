module dealerproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires json.simple;
    requires java.desktop;
    requires kotlin.stdlib;
    requires java.base;

    opens dealerproject to javafx.graphics, javafx.fxml; // Allow access to HelloApplication
    exports dealerproject;

    opens javafiles.gui to javafx.fxml; // Allow access to FXML files in the gui package
    exports javafiles.gui;
}