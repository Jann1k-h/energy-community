package at.technikum.UsageService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

// Bauplan zum Zerlegen einer erhaltenen Nachricht
public class ReceiveMessage {

    // final, da nach Erstellen Werte nicht mehr verändert werden
    private final String type;
    private final String association;
    private final double kwh;
    private final LocalDateTime datetime;

    public ReceiveMessage(String message) {

        // zerlegt den String bei jedem Semikolon
        String[] parts = message.split(";");

        this.type = parts[0];
        this.association = parts[1];
        this.kwh = Double.parseDouble(parts[2]);
        // aus String datetime wieder LocalDateTime machen und mit truncate auf ganze Stunden runden,
        // da Datenbank Einträge pro Stunde speichert
        this.datetime = LocalDateTime.parse(parts[3]).truncatedTo(ChronoUnit.HOURS);
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

    // printOutput zum Debuggen
    public String printOutput() {
        return "Type = " + type + "; Association = " + association + "; kwh = " + kwh + "; Datetime = " + datetime;
    }
}
