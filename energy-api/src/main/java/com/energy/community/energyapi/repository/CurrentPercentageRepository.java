package com.energy.community.energyapi.repository;

import com.energy.community.energyapi.model.CurrentPercentage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrentPercentageRepository extends JpaRepository<CurrentPercentage, Long> {

    Optional<CurrentPercentage> findTopByOrderByHourDesc();
}