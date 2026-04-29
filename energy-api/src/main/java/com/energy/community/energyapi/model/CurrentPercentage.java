package com.energy.community.energyapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "current_percentage")
public class CurrentPercentage {

    @Id
    private Long id;

    private LocalDateTime hour;

    private double communityDepleted;

    private double gridPortion;

    public Long getId() {
        return id;
    }

    public LocalDateTime getHour() {
        return hour;
    }

    public double getCommunityDepleted() {
        return communityDepleted;
    }

    public double getGridPortion() {
        return gridPortion;
    }
}