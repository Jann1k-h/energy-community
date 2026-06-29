module at.technikum.gui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.net.http;
    requires tools.jackson.databind;

    opens at.technikum.gui to javafx.fxml;
    exports at.technikum.gui;
}