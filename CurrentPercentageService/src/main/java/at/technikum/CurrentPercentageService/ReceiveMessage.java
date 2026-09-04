package at.technikum.CurrentPercentageService;

import java.time.LocalDateTime;

public class ReceiveMessage {

    // final, da nach Erstellen Werte nicht mehr verändert werden
    private final LocalDateTime hour;
    private final double communityProduced;
    private final double communityUsed;
    private final double gridUsed;

    public ReceiveMessage(String message) {

        // zerlegt die erhaltene Nachricht bei jedem Semikolon
        String[] parts = message.split(";");

        // Die Nachricht kommt vom Usage Service und enthält:
        // hour;communityProduced;communityUsed;gridUsed
        this.hour = LocalDateTime.parse(parts[0]);
        this.communityProduced = Double.parseDouble(parts[1]);
        this.communityUsed = Double.parseDouble(parts[2]);
        this.gridUsed = Double.parseDouble(parts[3]);
    }

    // printOutput zum Debuggen
    public String printOutput() {
        return "Hour = " + hour
                + "; Community Produced = " + communityProduced
                + "; Community Used = " + communityUsed
                + "; Grid Used = " + gridUsed;
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