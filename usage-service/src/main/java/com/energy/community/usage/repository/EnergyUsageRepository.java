package com.energy.community.usage.repository;

import com.energy.community.usage.model.EnergyUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EnergyUsageRepository extends JpaRepository<EnergyUsage, Long> {

    Optional<EnergyUsage> findByHour(LocalDateTime hour);
}