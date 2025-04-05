module dealerproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires json.simple;
    requires java.desktop;
    requires kotlin.stdlib;
    requires java.base;

    opens javafiles.gui to javafx.fxml;
    exports javafiles.gui;
    opens javafiles.domainfiles to javafx.base;//, org.mockito;
}