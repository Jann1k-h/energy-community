package com.energy.community.usage.service;

import com.energy.community.usage.model.EnergyMessage;
import com.energy.community.usage.model.EnergyUsage;
import com.energy.community.usage.repository.EnergyUsageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UsageService {

    private final EnergyUsageRepository repository;

    public UsageService(EnergyUsageRepository repository) {
        this.repository = repository;
    }

    public void processMessage(EnergyMessage message) {
        LocalDateTime hour = message.getDatetime()
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        EnergyUsage usage = repository.findByHour(hour)
                .orElse(new EnergyUsage(hour));

        if ("PRODUCER".equals(message.getType())) {
            usage.setCommunityProduced(
                    usage.getCommunityProduced() + message.getKwh()
            );
        }

        if ("USER".equals(message.getType())) {
            double availableCommunityEnergy =
                    usage.getCommunityProduced() - usage.getCommunityUsed();

            availableCommunityEnergy = Math.max(availableCommunityEnergy, 0);

            double communityPart = Math.min(message.getKwh(), availableCommunityEnergy);
            double gridPart = message.getKwh() - communityPart;

            usage.setCommunityUsed(usage.getCommunityUsed() + communityPart);
            usage.setGridUsed(usage.getGridUsed() + gridPart);
        }

        repository.save(usage);
    }
}