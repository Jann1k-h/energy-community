package com.energy.community.community.user.model;

import java.time.LocalDateTime;

public class EnergyMessage {

    private String type;
    private String association;
    private double kwh;
    private LocalDateTime datetime;

    public EnergyMessage() {
    }

    public EnergyMessage(String type, String association, double kwh, LocalDateTime datetime) {
        this.type = type;
        this.association = association;
        this.kwh = kwh;
        this.datetime = datetime;
    }

    public String getType() {
        return type;
    }

    public String getAssociation() {
        return association;
    }

    public double getKwh() {
        return kwh;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }
}