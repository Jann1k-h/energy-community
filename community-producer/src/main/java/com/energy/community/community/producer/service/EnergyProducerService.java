package com.energy.community.community.producer.service;

import com.energy.community.community.producer.model.EnergyMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class EnergyProducerService {

    // Obejekt zum Senden von Nachrichten an RabbitMQ
    private final RabbitTemplate rabbitTemplate;
    private final Random random = new Random();

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    // Konstruktor (wird von Spring automatisch injiziert)
    public EnergyProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendEnergyProduction() {
        int hour = LocalDateTime.now().getHour();

        double baseProduction;

        if (hour >= 6 && hour < 9) {
            baseProduction = 0.006;      // Morgen: Sonne kommt langsam
        } else if (hour >= 9 && hour < 16) {
            baseProduction = 0.020;      // Tag: viel Solarproduktion
        } else if (hour >= 16 && hour < 20) {
            baseProduction = 0.008;      // Abend: Sonne wird schwächer
        } else {
            baseProduction = 0.0002;     // Nacht: fast keine Produktion
        }

        double weatherFactor = 0.8 + random.nextDouble() * 0.4; // 0.8 bis 1.2
        double kwh = baseProduction * weatherFactor * 300;

        EnergyMessage message = new EnergyMessage(
                "PRODUCER",
                "COMMUNITY",
                kwh,
                LocalDateTime.now()
        );

        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);

        System.out.println("Produktion gesendet: " + kwh + " kWh");
    }
}