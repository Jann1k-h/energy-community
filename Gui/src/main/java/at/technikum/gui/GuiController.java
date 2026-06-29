package at.technikum.gui;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
    // Throws Exception, weil bei Aufruf Fehler entstehen können und JAVA zwingt diese zu behandeln
    private void refresh() throws Exception {
        String url = "http://localhost:8080/energy/current";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());

        double community_depleted = root.path("communityDepleted").asDouble();
        double grid_portion = root.path("gridPortion").asDouble();
        String hour = root.path("hour").asString();

        communityDepleted.setText(String.format("%.2f%%", community_depleted));
        gridPortion.setText(String.format("%.2f%%", grid_portion));

        System.out.println(communityDepleted);
        System.out.println(gridPortion);
    }


    @FXML
    // Throws Exception, weil bei Aufruf Fehler entstehen können und JAVA zwingt diese zu behandeln
    private void showData() throws Exception {
        String start = startDatePicker.getValue() + "T" + startTimePicker.getValue() + ":00";
        String end = endDatePicker.getValue() + "T" + endTimePicker.getValue() + ":00";

        String url = "http://localhost:8080/energy/historical?start=" + start + "&end=" + end;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());

        double community_produced = root.path("communityProduced").asDouble();
        double community_used = root.path("communityUsed").asDouble();
        double grid_used = root.path("gridUsed").asDouble();

        communityProduced.setText(community_produced + " kWh");
        communityUsed.setText(community_used + " kWh");
        gridUsed.setText(grid_used + " kWh");

        usageChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        series.getData().add(new XYChart.Data<>("Produced", community_produced));
        series.getData().add(new XYChart.Data<>("Used", community_used));
        series.getData().add(new XYChart.Data<>("Grid", grid_used));

        usageChart.getData().add(series);


        System.out.println(communityProduced);
        System.out.println(communityUsed);
        System.out.println(gridUsed);
    }
}