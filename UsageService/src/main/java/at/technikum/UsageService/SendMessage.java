package at.technikum.UsageService;

// Bauplan zum Versenden einer Nachricht, damit jede Nachricht im gleichen Format verschickt wird
public class SendMessage {

    // final, da nach Erstellen Werte nicht mehr verändert werden
    private final String text;
    private final String datetime;

    public SendMessage(String text, String datetime) {
        this.text = text;
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return text + ";" + datetime;
    }
}
