package com.energy.community.community.producer.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.io.Serializable;

// EnergyMessage kann in der Form nach RabbitMQ gesendet werden, daher implementieren wir Serializable
// Serializable ermöglicht es, dass das Objekt in einen Bytestream umgewandelt werden kann
public class EnergyMessage implements Serializable{

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