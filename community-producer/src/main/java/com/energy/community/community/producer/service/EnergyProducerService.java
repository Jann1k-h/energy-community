package com.energy.community.community.producer.service;

import com.energy.community.community.producer.model.EnergyMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class EnergyProducerService {

    // Obejekt zum Senden von Nachrichten an RabbitMQ
    private final RabbitTemplate rabbitTemplate;

    // Objekt zum Abrufen von Wetterdaten
    private final WeatherService weatherService;


    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    // Konstruktor (wird von Spring automatisch injiziert)
    public EnergyProducerService(RabbitTemplate rabbitTemplate, WeatherService weatherService) {
        this.rabbitTemplate = rabbitTemplate;
        this.weatherService = weatherService;
    }

    public void sendEnergyProduction() {

        int cloudCover = weatherService.getCloudCover();

        double sunFactor = (100 - cloudCover) / 100.0;
        double baseKwh = 0.002;
        double randomKwh = Math.random() * 0.004;
        double kwh = baseKwh + (randomKwh * sunFactor);
        kwh = Math.round(kwh * 100000.0) / 100000.0;

        EnergyMessage message = new EnergyMessage(
            "PRODUCER",
            "COMMUNITY",
            kwh,
            LocalDateTime.now()
        );

        // Nachricht an RabbitMQ senden
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);

        System.out.println("Gesendet: " + message.getKwh());
    }
}