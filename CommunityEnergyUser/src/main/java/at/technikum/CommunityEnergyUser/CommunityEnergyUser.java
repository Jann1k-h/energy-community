package at.technikum.CommunityEnergyUser;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service // Spring-Klasse mit Logik
public class CommunityEnergyUser {

    // Feld für Objekt RabbitTemplate deklarieren
    private final RabbitTemplate rabbitTemplate;

    // Konstruktor-Injection
    // CommunityEnergyUser braucht RabbitTemplate zum Versenden von Nachrichten
    // Spring Boot erstellt RabbitTemplate automatisch.
    // Spring Boot übergibt es in den Konstruktor.
    // Die Klasse speichert es in ihrer Variable.
    public CommunityEnergyUser(RabbitTemplate rabbitTemplate) {
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
            "USER",
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

    // Energieverbrauch anhand der aktuelle Uhrzeit bestimmen
    private double calculatedKwh() {

        int hour = LocalDateTime.now().getHour();

        double kwh;

        // Morgen Verbrauch höher
        if (hour >= 6 && hour <= 9) {
            kwh = 0.0008 + Math.random() * 0.0012;   // 0.0008 bis 0.0020 kWh

        // Abend höchster Verbrauch
        } else if (hour >= 17 && hour <= 22) {
            kwh = 0.0012 + Math.random() * 0.0020;   // 0.0012 bis 0.0032 kWh

        // Nacht sehr niedriger Verbrauch
        } else if (hour >= 0 && hour <= 5) {
            kwh = 0.0001 + Math.random() * 0.0004;   // 0.0001 bis 0.0005 kWh

        // ansonsten normaler Verbrauch
        } else {
            kwh = 0.0004 + Math.random() * 0.0008;   // 0.0004 bis 0.0012 kWh
        }

        return kwh;
    }
}