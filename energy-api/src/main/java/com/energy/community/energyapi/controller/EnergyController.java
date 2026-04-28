package com.energy.community.energyapi.controller;


import com.energy.community.energyapi.model.CurrentEnergyResponse;
import com.energy.community.energyapi.model.HistoricalEnergyResponse;
import com.energy.community.energyapi.service.EnergyService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class EnergyController {

    private final EnergyService energyService;

    public EnergyController(EnergyService energyService) {
        this.energyService = energyService;
    }

    @GetMapping("/energy/current")
    public CurrentEnergyResponse getCurrentEnergy() {
        return energyService.getCurrentEnergyData();
    }

    @GetMapping("/energy/historical")
    public List<HistoricalEnergyResponse> getHistoricalEnergy(
            @RequestParam String start,
            @RequestParam String end
    ) {
        LocalDateTime startDateTime = LocalDateTime.parse(start);
        LocalDateTime endDateTime = LocalDateTime.parse(end);

        return energyService.getHistoricalEnergyData(startDateTime, endDateTime);
    }
}