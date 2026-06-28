package at.technikum.CommunityEnergyProducer;

// Bauplan zum Versenden einer Nachricht, damit jede Nachricht im gleichen Format verschickt wird
public class SendMessage {

    // final, da nach Erstellen Werte nicht mehr verändert werden
    private final String type;
    private final String association;
    private final double kwh;
    private final String datetime;

    public SendMessage(String type, String association, double kwh, String datetime) {
        this.type = type;
        this.association = association;
        this.kwh = kwh;
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return type + ";" + association + ";" + kwh + ";" + datetime;
    }
}