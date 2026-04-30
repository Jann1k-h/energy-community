package com.energy.community.community.user.model;

import java.time.LocalDateTime;

// Klasse beschreibt den Aufbau einer EnergyMessage.
// Diese Nachricht wird über RabbitMQ empfangen und enthält Informationen zur Energieproduktion.
public class EnergyMessage {

    // --------------------------------------------------
    // Typ der Nachricht, z. B. "PRODUCER"
    private String type;

    // Zugehörigkeit / Gruppe, z. B. "COMMUNITY"
    private String association;

    // Produzierte oder verbrauchte Energiemenge in Kilowattstunden
    private double kwh;

    // Zeitpunkt, zu dem die Energiedaten erstellt wurden
    private LocalDateTime datetime;
    // --------------------------------------------------


    // --------------------------------------------------
    // Leerer Konstruktor
    // Wird benötigt, damit JSON-Daten automatisch in ein Java-Objekt umgewandelt werden können
    public EnergyMessage() {
    }
    // --------------------------------------------------


    // --------------------------------------------------
    // Konstruktor mit allen Feldern
    // Wird verwendet, wenn ein EnergyMessage-Objekt direkt mit Werten erstellt werden soll
    public EnergyMessage(String type, String association, double kwh, LocalDateTime datetime) {
        this.type = type;
        this.association = association;
        this.kwh = kwh;
        this.datetime = datetime;
    }
    // --------------------------------------------------


    // --------------------------------------------------
    // Gibt den Typ der Nachricht zurück
    public String getType() {
        return type;
    }

    // Gibt die Zugehörigkeit / Gruppe zurück
    public String getAssociation() {
        return association;
    }

    // Gibt die Energiemenge in kWh zurück
    public double getKwh() {
        return kwh;
    }

    // Gibt den Zeitpunkt der Nachricht zurück
    public LocalDateTime getDatetime() {
        return datetime;
    }
    // --------------------------------------------------
}