package at.technikum.CurrentPercentageService;

import java.time.LocalDateTime;

public class ReceiveMessage {

    private String text;
    private LocalDateTime datetime;

    public ReceiveMessage(String message) {

        String[] parts = message.split(";");

        this.text = parts[0];
        this.datetime = LocalDateTime.parse(parts[1]); // aus String datetime wieder LocalDateTime machen
    }

    public String printOutput() {
        return "Text = " + text + "; Datetime = " + datetime;
    }

    public String getType() {
        return text;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }
}
