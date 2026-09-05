package at.technikum.gui;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

public class GuiController {

    public Label communityDepleted;
    public Label gridPortion;
    public DatePicker startDatePicker;
    public ChoiceBox<String> startTimePicker;
    public DatePicker endDatePicker;
    public ChoiceBox<String> endTimePicker;
    public Label communityProduced;
    public Label communityUsed;
    public Label gridUsed;
    public BarChart<String, Number> usageChart;

    // EnergyApiClient übernimmt die HTTP-Kommunikation.
    // Der Controller ist dadurch nur noch für die GUI zuständig.
    private final EnergyApiClient energyApiClient = new EnergyApiClient();

    @FXML
    public void initialize() throws Exception {

        refresh();

        startTimePicker.getItems().addAll(
                "00:00", "01:00", "02:00", "03:00",
                "04:00", "05:00", "06:00", "07:00",
                "08:00", "09:00", "10:00", "11:00",
                "12:00", "13:00", "14:00", "15:00",
                "16:00", "17:00", "18:00", "19:00",
                "20:00", "21:00", "22:00", "23:00"
        );

        endTimePicker.getItems().addAll(
                "00:00", "01:00", "02:00", "03:00",
                "04:00", "05:00", "06:00", "07:00",
                "08:00", "09:00", "10:00", "11:00",
                "12:00", "13:00", "14:00", "15:00",
                "16:00", "17:00", "18:00", "19:00",
                "20:00", "21:00", "22:00", "23:00"
        );

        startTimePicker.setValue("08:00");
        endTimePicker.setValue("12:00");
    }

    @FXML
    // Aktualisiert die Anzeige der aktuellen Prozentwerte.
    private void refresh() throws Exception {
        CurrentPercentageDto currentPercentage =
                energyApiClient.getCurrentPercentage();

        communityDepleted.setText(
                String.format("%.2f%%", currentPercentage.communityDepleted())
        );

        gridPortion.setText(
                String.format("%.2f%%", currentPercentage.gridPortion())
        );
    }

    @FXML
    // Holt und zeigt die historischen Daten des gewählten Zeitraums.
    private void showData() throws Exception {
        String start = startDatePicker.getValue()
                + "T"
                + startTimePicker.getValue()
                + ":00";

        String end = endDatePicker.getValue()
                + "T"
                + endTimePicker.getValue()
                + ":00";

        HistoricalUsageDto historicalUsage =
                energyApiClient.getHistoricalUsage(start, end);

        double communityProducedValue =
                historicalUsage.communityProduced();

        double communityUsedValue =
                historicalUsage.communityUsed();

        double gridUsedValue =
                historicalUsage.gridUsed();

        communityProduced.setText(
                String.format("%.3f", communityProducedValue) + " kWh"
        );

        communityUsed.setText(
                String.format("%.3f", communityUsedValue) + " kWh"
        );

        gridUsed.setText(
                String.format("%.3f", gridUsedValue) + " kWh"
        );

        usageChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        series.getData().add(
                new XYChart.Data<>("Produced", communityProducedValue)
        );

        series.getData().add(
                new XYChart.Data<>("Used", communityUsedValue)
        );

        series.getData().add(
                new XYChart.Data<>("Grid", gridUsedValue)
        );

        usageChart.getData().add(series);
    }
}