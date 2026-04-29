package com.energy.community.community.user.service;

import com.energy.community.community.user.model.EnergyMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        int hour = LocalDateTime.now().getHour();

        double baseUsage;

        if (hour >= 6 && hour < 9) {
            baseUsage = 0.010;       // Morgen-Peak
        } else if (hour >= 9 && hour < 17) {
            baseUsage = 0.005;       // Tagsüber niedriger Verbrauch
        } else if (hour >= 17 && hour < 22) {
            baseUsage = 0.012;       // Abend-Peak
        } else {
            baseUsage = 0.006;       // Nacht: Grundverbrauch
        }

        double usageFactor = 0.8 + random.nextDouble() * 0.4; // 0.8 bis 1.2
        double kwh = baseUsage * usageFactor * 150;

        EnergyMessage message = new EnergyMessage(
                "USER",
                "COMMUNITY",
                kwh,
                LocalDateTime.now()
        );

        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);

        System.out.println("Verbrauch gesendet: " + kwh + " kWh");
    }
}