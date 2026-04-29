package com.energy.community.gui;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HistoricalResponse {

    private double communityProduced;
    private double communityUsed;
    private double gridUsed;

    public HistoricalResponse() {
    }

    public double getCommunityProduced() {
        return communityProduced;
    }

    public void setCommunityProduced(double communityProduced) {
        this.communityProduced = communityProduced;
    }

    public double getCommunityUsed() {
        return communityUsed;
    }

    public void setCommunityUsed(double communityUsed) {
        this.communityUsed = communityUsed;
    }

    public double getGridUsed() {
        return gridUsed;
    }

    public void setGridUsed(double gridUsed) {
        this.gridUsed = gridUsed;
    }
}