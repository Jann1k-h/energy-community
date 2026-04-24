package com.energy.community.community.producer.service;

import com.energy.community.community.producer.model.EnergyMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class EnergyProducerService {

    private final RabbitTemplate rabbitTemplate;
    private final WeatherService weatherService;
    private final Random random = new Random();

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public EnergyProducerService(RabbitTemplate rabbitTemplate, WeatherService weatherService) {
        this.rabbitTemplate = rabbitTemplate;
        this.weatherService = weatherService;
    }

    public void sendEnergyProduction() {
        int cloudCover = weatherService.getCloudCover();

        double sunFactor = (100 - cloudCover) / 100.0;

        double baseKwh = 0.002;
        double randomKwh = random.nextDouble() * 0.004;

        double kwh = baseKwh + (randomKwh * sunFactor);
        kwh = Math.round(kwh * 100000.0) / 100000.0;

        String message = "{"
        + "\"type\":\"PRODUCER\","
        + "\"association\":\"COMMUNITY\","
        + "\"kwh\":" + kwh + ","
        + "\"datetime\":\"" + LocalDateTime.now() + "\""
        + "}";

        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);

        System.out.println("Gesendet: " + message);
    }
}