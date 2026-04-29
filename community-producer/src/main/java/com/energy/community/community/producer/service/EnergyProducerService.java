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
        double kwh = 1 + random.nextDouble() * 5;

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