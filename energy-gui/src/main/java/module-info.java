module com.energy.community.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;

    opens com.energy.community.gui to javafx.fxml, com.fasterxml.jackson.databind;
    exports com.energy.community.gui;
}