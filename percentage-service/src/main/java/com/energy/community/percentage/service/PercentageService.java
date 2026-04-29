package com.energy.community.percentage.service;

import com.energy.community.percentage.model.CurrentPercentage;
import com.energy.community.percentage.model.EnergyUsage;
import com.energy.community.percentage.repository.CurrentPercentageRepository;
import com.energy.community.percentage.repository.EnergyUsageRepository;
import org.springframework.stereotype.Service;

@Service
public class PercentageService {

    private final EnergyUsageRepository energyUsageRepository;
    private final CurrentPercentageRepository currentPercentageRepository;

    public PercentageService(
            EnergyUsageRepository energyUsageRepository,
            CurrentPercentageRepository currentPercentageRepository
    ) {
        this.energyUsageRepository = energyUsageRepository;
        this.currentPercentageRepository = currentPercentageRepository;
    }

    public void calculateCurrentPercentage() {
        EnergyUsage usage = energyUsageRepository.findTopByOrderByHourDesc()
                .orElse(null);

        if (usage == null) {
            return;
        }

        double communityProduced = usage.getCommunityProduced();
        double communityUsed = usage.getCommunityUsed();
        double gridUsed = usage.getGridUsed();

        double totalUsed = communityUsed + gridUsed;

        double communityDepleted = 0;
        double gridPortion = 0;

        if (communityProduced > 0) {
            communityDepleted = (communityUsed / communityProduced) * 100;
            communityDepleted = Math.min(communityDepleted, 100);
        }

        if (totalUsed > 0) {
            gridPortion = (gridUsed / totalUsed) * 100;
        }

        CurrentPercentage currentPercentage =
                currentPercentageRepository.findByHour(usage.getHour())
                        .orElse(new CurrentPercentage());

        currentPercentage.setHour(usage.getHour());
        currentPercentage.setCommunityDepleted(communityDepleted);
        currentPercentage.setGridPortion(gridPortion);

        currentPercentageRepository.save(currentPercentage);

        System.out.println(
                "Prozentwerte berechnet: communityDepleted="
                        + communityDepleted
                        + "%, gridPortion="
                        + gridPortion
                        + "%"
        );
    }
}