package com.energy.community.energyapi.controller;

import com.energy.community.energyapi.model.CurrentPercentage;
import com.energy.community.energyapi.model.EnergyUsage;
import com.energy.community.energyapi.repository.CurrentPercentageRepository;
import com.energy.community.energyapi.repository.EnergyUsageRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class EnergyController {

    private final CurrentPercentageRepository currentPercentageRepository;
    private final EnergyUsageRepository energyUsageRepository;

    public EnergyController(
            CurrentPercentageRepository currentPercentageRepository,
            EnergyUsageRepository energyUsageRepository
    ) {
        this.currentPercentageRepository = currentPercentageRepository;
        this.energyUsageRepository = energyUsageRepository;
    }

    @GetMapping("/energy/current")
    public CurrentPercentage getCurrentPercentage() {
        return currentPercentageRepository.findTopByOrderByHourDesc()
                .orElse(null);
    }

    @GetMapping("/energy/historical")
    public HistoricalEnergyResponse getHistoricalData(
            @RequestParam String start,
            @RequestParam String end
    ) {
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);

        List<EnergyUsage> usages = energyUsageRepository.findByHourBetween(startDate, endDate);

        double communityProduced = 0;
        double communityUsed = 0;
        double gridUsed = 0;

        for (EnergyUsage usage : usages) {
            communityProduced += usage.getCommunityProduced();
            communityUsed += usage.getCommunityUsed();
            gridUsed += usage.getGridUsed();
        }

        return new HistoricalEnergyResponse(
                communityProduced,
                communityUsed,
                gridUsed
        );
    }

    public record HistoricalEnergyResponse(
            double communityProduced,
            double communityUsed,
            double gridUsed
    ) {
    }
}