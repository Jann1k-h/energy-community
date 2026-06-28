package at.technikum.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        // FXMLLoader lädt die FXML-Datei, in der das Layout der GUI definiert ist
        FXMLLoader fxmlLoader = new FXMLLoader(
                GuiApplication.class.getResource("/at/technikum/gui/energy-view.fxml")
        );

        // Scene erstellen und geladene FXML-GUI als Inhalt setzen
        // 600 = Breite, 700 = Höhe des Fensters
        Scene scene = new Scene(fxmlLoader.load(), 600, 700);

        // Fenstertitel setzen
        stage.setTitle("Energy Community");

        // Scene in das Fenster setzen
        stage.setScene(scene);

        // Fenster anzeigen
        stage.show();
    }
}