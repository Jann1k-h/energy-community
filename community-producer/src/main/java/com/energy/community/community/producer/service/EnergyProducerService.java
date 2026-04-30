package com.energy.community.community.producer.service;

import com.energy.community.community.producer.model.EnergyMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

// Klasse enthält die Logik zum Erstellen und Senden von Energieproduktionsdaten
@Service
public class EnergyProducerService {

    // --------------------------------------------------
    // RabbitTemplate ist das zentrale Objekt zum Senden von Nachrichten an RabbitMQ
    private final RabbitTemplate rabbitTemplate;

    // WeatherService wird verwendet, um die aktuelle Bewölkung von der Wetter-API zu holen
    private final WeatherService weatherService;

    // Random für Zufallsschwankungen
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
    // Spring übergibt automatisch RabbitTemplate und WeatherService
    // Dadurch kann diese Klasse Nachrichten senden und Wetterdaten abrufen
    public EnergyProducerService(
            RabbitTemplate rabbitTemplate,
            WeatherService weatherService
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.weatherService = weatherService;
    }
    // --------------------------------------------------


    // --------------------------------------------------
    // Methode erstellt Energieproduktionsdaten und sendet sie an RabbitMQ
    public void sendEnergyProduction() {

        // Aktuelle Stunde ermitteln
        int hour = LocalDateTime.now().getHour();

        // Basisproduktion in kWh
        double baseProduction;

        // Morgen: Sonne kommt langsam --> geringe Produktion
        if (hour >= 6 && hour < 9) {
            baseProduction = 0.004;

        // Tag: viel Sonnenlicht --> hohe Solarproduktion
        } else if (hour >= 9 && hour < 16) {
            baseProduction = 0.018;

        // Abend: Sonne wird schwächer --> Produktion sinkt
        } else if (hour >= 16 && hour < 20) {
            baseProduction = 0.006;

        // Nacht: fast keine Solarproduktion
        } else {
            baseProduction = 0.0002;
        }

        // Aktuelle Bewölkung von der Wetter-API holen
        // Wert liegt zwischen 0 und 100
        // 0 = keine Wolken, 100 = vollständig bewölkt
        int cloudCover = weatherService.getCloudCover();

        // Cloud-Faktor berechnen
        // Je höher die Bewölkung, desto niedriger die Produktion
        // Bei 0% Wolken bleibt Faktor 1.0
        // Bei 100% Wolken bleibt trotzdem noch 30% Produktion übrig
        double cloudFactor = 1.0 - (cloudCover / 100.0 * 0.7);

        // Zufallsfaktor für kleine natürliche Schwankungen
        // Wert liegt zwischen 0.95 und 1.05
        double randomFactor = 0.95 + random.nextDouble() * 0.1;

        // Tatsächliche Energieproduktion in kWh wird berechnet
        double kwh = baseProduction * cloudFactor * randomFactor * 100;

        // EnergyMessage-Objekt wird erstellt
        // Dieses Objekt enthält Sender, Empfänger, produzierte Energiemenge und Zeitpunkt
        EnergyMessage message = new EnergyMessage(
                "PRODUCER",
                "COMMUNITY",
                kwh,
                LocalDateTime.now()
        );

        // Nachricht wird an RabbitMQ gesendet
        // Gesendet wird an den Exchange mit dem angegebenen Routing Key
        // Der MessageConverter wandelt das Java-Objekt automatisch in JSON um
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);

        // Ausgabe in der Konsole zur Kontrolle
        System.out.println("Produktion gesendet: " + kwh + " kWh | Bewölkung: " + cloudCover + "%");
    }
    // --------------------------------------------------
}