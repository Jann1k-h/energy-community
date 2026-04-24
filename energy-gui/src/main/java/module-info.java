module com.energy.community {
    requires javafx.controls;
    requires javafx.fxml;
    
    opens com.energy.community.gui to javafx.fxml;
    exports com.energy.community.gui;
}