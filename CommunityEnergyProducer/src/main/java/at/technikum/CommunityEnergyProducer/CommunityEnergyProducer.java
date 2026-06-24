package at.technikum.CommunityEnergyProducer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

@Service // Spring-Klasse mit Logik
public class CommunityEnergyProducer {

    private final RabbitTemplate rabbitTemplate;

    // Constructor holt sich von Spring RabbitTemplate, damit Producer Nachrichten senden kann
    public CommunityEnergyProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedRate = 3000)
    public void sendMessage() throws Exception {

        SendMessage message = new SendMessage(
            "PRODUCER",
            "COMMUNITY",
            calculatedKwh(),
            LocalDateTime.now().toString()
        );

        // RabbitMQ kann kein Java Objekt verstehen, deswegen als String versenden toString() in jeweiligen SendMessage Klasse festgelegt
        String stringMessage = message.toString();

        rabbitTemplate.convertAndSend(RabbitConfig.ENERGY_QUEUE, stringMessage);

        System.out.println("Nachricht gesendet: " + stringMessage);
    }

    // Throws Exception, weil bei Aufruf Fehler entstehen können und JAVA zwingt diese zu behandeln
    private int getCurrentCloudCover() throws Exception {
        String url = "https://api.open-meteo.com/v1/forecast?latitude=48.239166&longitude=16.377441&hourly=cloud_cover&timezone=Europe%2FBerlin&forecast_days=1";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // JSON parsen
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());

        int hour = LocalDateTime.now().getHour();

        return root.path("hourly").path("cloud_cover").get(hour).asInt();
    }

    // Produktion bestimmen
    private double calculatedKwh() throws Exception {

        int cloudCover = getCurrentCloudCover(); // Wert von 0 bis 100

        double kwh;

        // wenig Wolken --> hohe Solarproduktion
        if (cloudCover <= 20) {
            kwh = 0.0020 + Math.random() * 0.0020;   // 0.0020 bis 0.0040 kWh

        // teilweise bewölkt --> mittlere Produktion
        } else if (cloudCover <= 50) {
            kwh = 0.0012 + Math.random() * 0.0015;   // 0.0012 bis 0.0027 kWh

        // stark bewölkt --> geringe Produktion
        } else if (cloudCover <= 80) {
            kwh = 0.0004 + Math.random() * 0.0010;   // 0.0004 bis 0.0014 kWh

        // sehr stark bewölkt --> kaum Produktion
        } else {
            kwh = 0.0001 + Math.random() * 0.0004;   // 0.0001 bis 0.0005 kWh
        }

        return kwh;
    }
}