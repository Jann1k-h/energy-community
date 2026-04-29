package com.energy.community.energyapi.repository;

import com.energy.community.energyapi.model.EnergyUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EnergyUsageRepository extends JpaRepository<EnergyUsage, Long> {

    List<EnergyUsage> findByHourBetween(LocalDateTime start, LocalDateTime end);
}