package at.technikum.UsageService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ReceiveMessage {

    private String type;
    private String association;
    private double kwh;
    private LocalDateTime datetime;

    public ReceiveMessage(String message) {

        String[] parts = message.split(";");

        this.type = parts[0];
        this.association = parts[1];
        this.kwh = Double.parseDouble(parts[2]);
        this.datetime = LocalDateTime.parse(parts[3]).truncatedTo(ChronoUnit.HOURS); // aus String datetime wieder LocalDateTime machen und mit truncate auf ganze Stunden runden
    }

    public String printOutput() {
        return "Type = " + type + "; Association = " + association + "; kwh = " + kwh + "; Datetime = " + datetime;
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
