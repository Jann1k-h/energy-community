package at.technikum.UsageService;

// Bauplan zum Versenden einer Nachricht, damit jede Nachricht im gleichen Format verschickt wird


import java.time.LocalDateTime;

public class SendMessage {

    private final LocalDateTime hour;
    private final double communityProduced;
    private final double communityUsed;
    private final double gridUsed;

    public SendMessage(
            LocalDateTime hour,
            double communityProduced,
            double communityUsed,
            double gridUsed
    ) {
        this.hour = hour;
        this.communityProduced = communityProduced;
        this.communityUsed = communityUsed;
        this.gridUsed = gridUsed;
    }

    @Override
    public String toString() {
        return hour + ";"
                + communityProduced + ";"
                + communityUsed + ";"
                + gridUsed;
    }
}