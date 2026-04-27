package com.energy.community.community.producer.scheduler;

import com.energy.community.community.producer.service.EnergyProducerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EnergyProducerScheduler {

    // alle 5 Sekunden wird die Methode produceEnergy() aufgerufen, die send EnergyProduction()
    // in EnergyProducerService aufruft, um Energieproduktionsdaten zu senden

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