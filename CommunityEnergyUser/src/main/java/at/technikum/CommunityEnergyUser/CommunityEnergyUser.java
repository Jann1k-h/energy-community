package at.technikum.CommunityEnergyUser;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service // Spring-Klasse mit Logik
public class CommunityEnergyUser {

    // EnergyMessagePublisher ist für das Senden
// der RabbitMQ-Nachrichten zuständig.
    private final EnergyMessagePublisher messagePublisher;

    public CommunityEnergyUser(
            EnergyMessagePublisher messagePublisher
    ) {
        this.messagePublisher = messagePublisher;
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

        // Die fertige Nachricht wird vom EnergyMessagePublisher
// an RabbitMQ gesendet.
        messagePublisher.publish(message);

        System.out.println("Nachricht gesendet: " + message);
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
        } else if ( hour <= 5) {
            kwh = 0.0001 + Math.random() * 0.0004;   // 0.0001 bis 0.0005 kWh

        // ansonsten normaler Verbrauch
        } else {
            kwh = 0.0004 + Math.random() * 0.0008;   // 0.0004 bis 0.0012 kWh
        }

        return kwh;
    }
}