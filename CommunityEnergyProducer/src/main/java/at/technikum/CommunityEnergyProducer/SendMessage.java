package at.technikum.CommunityEnergyProducer;

public class SendMessage {

    private String type;
    private String association;
    private double kwh;
    private String datetime;

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
