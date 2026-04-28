package com.energy.community.energyapi.service;


import com.energy.community.energyapi.model.CurrentEnergyResponse;
import com.energy.community.energyapi.model.HistoricalEnergyResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EnergyService {

    public CurrentEnergyResponse getCurrentEnergyData() {
        LocalDateTime currentHour = LocalDateTime.now()
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        return new CurrentEnergyResponse(
                currentHour,
                82.5,
                17.5
        );
    }

    public List<HistoricalEnergyResponse> getHistoricalEnergyData(
            LocalDateTime start,
            LocalDateTime end
    ) {
        List<HistoricalEnergyResponse> historicalData = new ArrayList<>();

        LocalDateTime currentHour = start.withMinute(0).withSecond(0).withNano(0);

        while (!currentHour.isAfter(end)) {
            historicalData.add(new HistoricalEnergyResponse(
                    currentHour,
                    18.05,
                    15.75,
                    2.30
            ));

            currentHour = currentHour.plusHours(1);
        }

        return historicalData;
    }
}