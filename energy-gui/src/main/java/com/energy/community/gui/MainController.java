package com.energy.community.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainController {

    @FXML
    private Label communityPoolLabel;

    @FXML
    private Label gridPortionLabel;

    @FXML
    private TextField startField;

    @FXML
    private TextField endField;

    @FXML
    private Label communityProducedLabel;

    @FXML
    private Label communityUsedLabel;

    @FXML
    private Label gridUsedLabel;

    private final ApiService apiService = new ApiService();

    private final DateTimeFormatter displayFormatter =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH");

    @FXML
    public void initialize() {
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        LocalDateTime oneDayAgo = now.minusHours(24);

        startField.setText(oneDayAgo.format(displayFormatter));
        endField.setText(now.format(displayFormatter));

        communityPoolLabel.setText("-");
        gridPortionLabel.setText("-");
        communityProducedLabel.setText("-");
        communityUsedLabel.setText("-");
        gridUsedLabel.setText("-");
    }

    @FXML
    private void refreshCurrentData() {
        try {
            CurrentResponse response = apiService.getCurrentData();

            communityPoolLabel.setText(String.format("%.2f%% used", response.getCommunityDepleted()));
            gridPortionLabel.setText(String.format("%.2f%%", response.getGridPortion()));

        } catch (Exception e) {
            showError("Fehler beim Laden der aktuellen Daten", e.getMessage());
        }
    }

    @FXML
    private void showHistoricalData() {
        try {
            LocalDateTime start = LocalDateTime.parse(startField.getText(), displayFormatter);
            LocalDateTime end = LocalDateTime.parse(endField.getText(), displayFormatter);

            HistoricalResponse response = apiService.getHistoricalData(start.toString(), end.toString());

            communityProducedLabel.setText(String.format("%.3f kWh", response.getCommunityProduced()));
            communityUsedLabel.setText(String.format("%.3f kWh", response.getCommunityUsed()));
            gridUsedLabel.setText(String.format("%.3f kWh", response.getGridUsed()));

        } catch (Exception e) {
            showError(
                    "Fehler beim Laden der historischen Daten",
                    "Bitte Datum im Format dd.MM.yyyy HH eingeben.\n\nDetails: " + e.getMessage()
            );
        }
    }

    private void showError(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText(title);
        alert.setContentText(text);
        alert.showAndWait();
    }
}