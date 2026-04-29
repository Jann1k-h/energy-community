package com.energy.community.energyapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "energy_usage")
public class EnergyUsage {

    @Id
    private Long id;

    private LocalDateTime hour;

    private double communityProduced;

    private double communityUsed;

    private double gridUsed;

    public Long getId() {
        return id;
    }

    public LocalDateTime getHour() {
        return hour;
    }

    public double getCommunityProduced() {
        return communityProduced;
    }

    public double getCommunityUsed() {
        return communityUsed;
    }

    public double getGridUsed() {
        return gridUsed;
    }
}