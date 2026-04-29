package com.energy.community.percentage.repository;

import com.energy.community.percentage.model.CurrentPercentage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CurrentPercentageRepository extends JpaRepository<CurrentPercentage, Long> {

    Optional<CurrentPercentage> findByHour(LocalDateTime hour);
}