package com.energy.community.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EnergyGuiApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(
                EnergyGuiApplication.class.getResource("primary.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load(), 420, 360);
        stage.setTitle("Energy GUI");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}