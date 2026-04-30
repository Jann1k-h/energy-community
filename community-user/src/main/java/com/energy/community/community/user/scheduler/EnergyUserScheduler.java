package com.energy.community.community.user.scheduler;

import com.energy.community.community.user.service.EnergyUserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// Klasse wird von Spring automatisch erkannt und verwaltet
// Sie startet regelmäßig das Senden von Energieverbrauchsdaten
@Component
public class EnergyUserScheduler {

    // --------------------------------------------------
    // EnergyUserService enthält die Logik zum Erstellen und Senden von Verbrauchsdaten
    private final EnergyUserService energyUserService;
    // --------------------------------------------------


    // --------------------------------------------------
    // Konstruktor
    // Spring übergibt automatisch den EnergyUserService
    // Dadurch kann diese Klasse den Service verwenden
    public EnergyUserScheduler(EnergyUserService energyUserService) {
        this.energyUserService = energyUserService;
    }
    // --------------------------------------------------


    // --------------------------------------------------
    // Diese Methode wird automatisch alle 5 Sekunden ausgeführt
    // fixedRate = 5000 bedeutet: alle 5000 Millisekunden wird die Methode erneut gestartet
    @Scheduled(fixedRate = 5000)
    public void useEnergy() {

        // Ruft die Service-Methode auf,
        // die Energieverbrauchsdaten erstellt und an RabbitMQ sendet
        energyUserService.sendEnergyUsage();
    }
    // --------------------------------------------------
}