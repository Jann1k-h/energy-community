package com.energy.community.usage.consumer;

import com.energy.community.usage.model.EnergyMessage;
import com.energy.community.usage.service.UsageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EnergyMessageConsumer {

    private final UsageService usageService;

    public EnergyMessageConsumer(UsageService usageService) {
        this.usageService = usageService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receiveMessage(EnergyMessage message) {

        System.out.println("Nachricht empfangen: " + message.getType() + " " + message.getKwh());

        usageService.processMessage(message);
    }
}