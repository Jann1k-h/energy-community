package com.energy.community.community.user.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

@Service
public class EnergyUserService {

    private final RabbitTemplate rabbitTemplate;
    private final Random random = new Random();

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public EnergyUserService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendEnergyUsage() {
        double kwh = calculateKwhUsage();

        String message = "{"
                + "\"type\":\"USER\","
                + "\"association\":\"COMMUNITY\","
                + "\"kwh\":" + kwh + ","
                + "\"datetime\":\"" + LocalDateTime.now() + "\""
                + "}";

        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);

        System.out.println("Gesendet: " + message);
    }

    private double calculateKwhUsage() {
        LocalTime now = LocalTime.now();
        int hour = now.getHour();

        double baseKwh = 0.001;
        double randomKwh = random.nextDouble() * 0.002;

        double peakFactor = 1.0;

        if (hour >= 6 && hour <= 9) {
            peakFactor = 2.0;
        } else if (hour >= 17 && hour <= 21) {
            peakFactor = 2.5;
        } else if (hour >= 0 && hour <= 5) {
            peakFactor = 0.6;
        }

        double kwh = (baseKwh + randomKwh) * peakFactor;

        return Math.round(kwh * 100000.0) / 100000.0;
    }
}