package com.energy.community.percentage.scheduler;

import com.energy.community.percentage.service.PercentageService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PercentageScheduler {

    private final PercentageService percentageService;

    public PercentageScheduler(PercentageService percentageService) {
        this.percentageService = percentageService;
    }

    // alle 5 Sekunden werden die Prozentwerte neu berechnet
    @Scheduled(fixedRate = 5000)
    public void calculate() {
        percentageService.calculateCurrentPercentage();
    }
}