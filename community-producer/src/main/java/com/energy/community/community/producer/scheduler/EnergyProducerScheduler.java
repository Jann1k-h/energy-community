package com.energy.community.community.producer.scheduler;

import com.energy.community.community.producer.service.EnergyProducerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EnergyProducerScheduler {

    // die Klasse sendet alle 5 Sekunden Anfrage an service/EnergyProducerService,
    // um neue Produktionsdaten zu generieren.
    // EnergyProducerService sendet die Daten dann an RabbitMQ

    private final EnergyProducerService energyProducerService;

    // Konstruktor
    public EnergyProducerScheduler(EnergyProducerService energyProducerService) {
        this.energyProducerService = energyProducerService;
    }

    @Scheduled(fixedRate = 5000)
    public void produceEnergy() {
        energyProducerService.sendEnergyProduction();
    }
}