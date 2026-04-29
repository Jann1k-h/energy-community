package com.energy.community.community.user.scheduler;

import com.energy.community.community.user.service.EnergyUserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EnergyUserScheduler {

    // alle 5 Sekunden wird die Methode useEnergy() aufgerufen
    // diese ruft sendEnergyUsage() im EnergyUserService auf

    private final EnergyUserService energyUserService;

    // Konstruktor
    public EnergyUserScheduler(EnergyUserService energyUserService) {
        this.energyUserService = energyUserService;
    }

    @Scheduled(fixedRate = 5000)
    public void useEnergy() {
        energyUserService.sendEnergyUsage();
    }
}