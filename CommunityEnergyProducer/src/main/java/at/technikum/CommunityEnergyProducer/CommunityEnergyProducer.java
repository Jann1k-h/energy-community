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
import java.util.concurrent.ThreadLocalRandom;

@Service // Spring-Klasse mit Logik
public class CommunityEnergyProducer {

    // Feld für Objekt RabbitTemplate deklarieren
    private final RabbitTemplate rabbitTemplate;

    // Konstruktor-Injection
    // CommunityEnergyProducer braucht RabbitTemplate zum Versenden von Nachrichten
    // Spring Boot erstellt RabbitTemplate automatisch.
    // Spring Boot übergibt es in den Konstruktor.
    // Die Klasse speichert es in ihrer Variable.
    public CommunityEnergyProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // Methode wird automatisch alle 3000 Millisekunden ausgeführt
    @Scheduled(fixedRate = 1000)
    public void sendMessage() throws Exception {

        // Random Delay zwischen 1 und 5 Sekunden
        int delay = ThreadLocalRandom.current().nextInt(0, 4001);
        Thread.sleep(delay);

        // Message-Objekt erstellen
        SendMessage message = new SendMessage(
            "PRODUCER",
            "COMMUNITY",
            calculatedKwh(),
            LocalDateTime.now().toString()
        );

        // Nachricht wird als String versendet
        // toString() ist in SendMessage-Klasse festgelegt
        String stringMessage = message.toString();

        // Sendet stringMessage an die Queue energy.queue
        rabbitTemplate.convertAndSend(RabbitConfig.ENERGY_QUEUE, stringMessage);

        // Ausgabe in Konsole
        System.out.println("Nachricht gesendet: " + stringMessage);
    }

    // Produktion anhand der Bewölkung bestimmen
    private double calculatedKwh() throws Exception {

        int cloudCover = getCurrentCloudCover(); // Wert von 0 bis 100
        int hour = LocalDateTime.now().getHour();
        double kwh;

        // nachts keine Solarproduktion
        if (hour < 6 || hour > 20) {
            kwh = 0.0;
            return kwh;
        }

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

    // Bewölkung anhand der aktuellen Stunde der Wetter-API bestimmen
    // Throws Exception, weil bei Aufruf Fehler entstehen können und Java zwingt diese zu behandeln
    private int getCurrentCloudCover() throws Exception {

        // URL zur Open-Meteo-API (Standort Wien, FH Technikum Wien
        String url = "https://api.open-meteo.com/v1/forecast?latitude=48.239166&longitude=16.377441&hourly=cloud_cover&timezone=Europe%2FBerlin&forecast_days=1";

        // Erstellt HTTP-Client Objekt, mit dem Java HTTP-Anfragen schicken kann
        HttpClient client = HttpClient.newHttpClient();

        // Anfrage bauen, wobei URI.create(url) String in URI umwandelt, die Java für HTTP-Anfragen verwenden kann
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        // Anfrage senden und Antwort als String speichern
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Antwort ist JSON, ObjectMapper liest JSON Text und erzeugt Navigationsbaum
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());

        // Aktuelle Stunde holen
        int hour = LocalDateTime.now().getHour();

        // Bewölkung für diese Stunde zurückgeben
        return root.path("hourly").path("cloud_cover").get(hour).asInt();
    }
}