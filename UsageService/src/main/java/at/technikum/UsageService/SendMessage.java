package at.technikum.UsageService;

public class SendMessage {

    private String text;
    private String datetime;

    public SendMessage(String text, String datetime) {
        this.text = text;
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return text + ";" + datetime;
    }
}
