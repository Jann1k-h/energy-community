package com.energy.community.community.user.service;

import com.energy.community.community.user.model.EnergyMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

// Klasse enthält die Logik zum Erstellen und Senden von Energieverbrauchsdaten
@Service
public class EnergyUserService {

    // --------------------------------------------------
    // RabbitTemplate ist das zentrale Objekt zum Senden von Nachrichten an RabbitMQ
    private final RabbitTemplate rabbitTemplate;

    // Random wird verwendet, um den Verbrauch leicht zufällig zu verändern
    // Dadurch wirkt der Energieverbrauch realistischer
    private final Random random = new Random();
    // --------------------------------------------------


    // --------------------------------------------------
    // Werte für Exchange und Routing Key werden aus der application.properties Datei geladen
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;
    // --------------------------------------------------


    // --------------------------------------------------
    // Konstruktor
    // Spring übergibt automatisch das RabbitTemplate
    // Dadurch kann diese Klasse Nachrichten an RabbitMQ senden
    public EnergyUserService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    // --------------------------------------------------


    // --------------------------------------------------
    // Methode erstellt Energieverbrauchsdaten und sendet sie an RabbitMQ
    public void sendEnergyUsage() {

        // Aktuelle Stunde ermitteln
        int hour = LocalDateTime.now().getHour();

        // Basisverbrauch in kWh
        double baseUsage;

        // Morgen: hoher Verbrauch, z. B. Licht, Küche, Dusche, Geräte
        if (hour >= 6 && hour < 9) {
            baseUsage = 0.012;

        // Tagsüber: niedrigerer Verbrauch
        } else if (hour >= 9 && hour < 17) {
            baseUsage = 0.007;

        // Abend: hoher Verbrauch, z. B. Kochen, Heizung, Unterhaltung
        } else if (hour >= 17 && hour < 22) {
            baseUsage = 0.015;

        // Nacht: Grundverbrauch, z. B. Kühlschrank, Router, Standby-Geräte
        } else {
            baseUsage = 0.006;
        }

        // Verbrauchsfaktor wird zufällig berechnet
        // Wert liegt zwischen 0.8 und 1.2
        // Dadurch wird ein leicht schwankender Verbrauch simuliert
        double usageFactor = 0.8 + random.nextDouble() * 0.4;

        // Tatsächlicher Energieverbrauch in kWh wird berechnet
        double kwh = baseUsage * usageFactor * 150;

        // EnergyMessage-Objekt wird erstellt
        // Dieses Objekt enthält Typ, Zugehörigkeit, Verbrauchsmenge und Zeitpunkt
        EnergyMessage message = new EnergyMessage(
                "USER",
                "COMMUNITY",
                kwh,
                LocalDateTime.now()
        );

        // Nachricht wird an RabbitMQ gesendet
        // Gesendet wird an den Exchange mit dem angegebenen Routing Key
        // Der MessageConverter wandelt das Java-Objekt automatisch in JSON um
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);

        // Ausgabe in der Konsole zur Kontrolle
        System.out.println("Verbrauch gesendet: " + kwh + " kWh");
    }
    // --------------------------------------------------
}