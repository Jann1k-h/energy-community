package at.technikum.CurrentPercentageService;

import at.technikum.CurrentPercentageService.db.CurrentPercentageTable;
import at.technikum.CurrentPercentageService.db.CurrentPercentageTableRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service // Spring-Klasse mit Logik
public class CurrentPercentageService {

    // Feld für Objekt CurrentPercentageTableRepository deklarieren,
    // ohne dem kann CurrentPercentageService nicht auf
    // currentPercentageTableRepository zugreifen und Befehle wie
    // .findById und .save ausführen.
    private final CurrentPercentageTableRepository currentPercentageTableRepository;

    // --------------------------------------------------
    // Konstruktor-Injection
    //
    // Die Klasse erstellt benötigte Objekte nicht selbst mit "new",
    // sondern bekommt sie von Spring Boot übergeben.
    // --> Objekte sind bereits richtig erstellt, konfiguriert und verwaltet.
    //
    // CurrentPercentageService braucht nur sein eigenes Repository.
    // Er bekommt die Werte für die Berechnung über RabbitMQ und
    // hat keinen Zugriff mehr auf die Usage-Tabelle.
    public CurrentPercentageService(
            CurrentPercentageTableRepository currentPercentageTableRepository
    ) {
        this.currentPercentageTableRepository = currentPercentageTableRepository;
    }
    // --------------------------------------------------

    @RabbitListener(queues = RabbitConfig.USAGE_UPDATE_QUEUE)
    // Wenn in der Queue eine Nachricht ankommt, wird diese Methode aufgerufen
    // und ihr Inhalt als Parameter übergeben.
    public void receiveMessage(String message) {

        // String message wird in ReceiveMessage-Objekt umgewandelt.
        // Dieses enthält die bereits berechneten Werte vom Usage Service.
        ReceiveMessage receiveMessage = new ReceiveMessage(message);

        // Feld für Objekt CurrentPercentageTable deklarieren
        CurrentPercentageTable currentPercentageTableEntry;

        // --------------------------------------------------
        // Wenn in der Percentage-Tabelle bereits ein Eintrag für diese Stunde
        // existiert, wird er geholt.
        if (currentPercentageTableRepository
                .findById(receiveMessage.getHour())
                .isPresent()) {

            currentPercentageTableEntry = currentPercentageTableRepository
                    .findById(receiveMessage.getHour())
                    .get();

            // Ansonsten wird ein neuer Eintrag in der Percentage-Tabelle erzeugt.
        } else {
            // Java-Objekt erstellen für neue Tabellenzeile
            currentPercentageTableEntry = new CurrentPercentageTable();

            // In dem neuen Eintrag Stunde setzen
            currentPercentageTableEntry.setHour(receiveMessage.getHour());
        }
        // --------------------------------------------------

        // Community-Depleted berechnen.
        // Gibt an, wie viel Prozent der produzierten Community-Energie
        // bereits verbraucht wurde.
        //
        // Wenn noch keine Energie produziert wurde, wird 0 gesetzt,
        // damit keine Division durch 0 entsteht.
        double communityDepleted = receiveMessage.getCommunityProduced() > 0
                ? receiveMessage.getCommunityUsed()
                / receiveMessage.getCommunityProduced() * 100
                : 0;

        currentPercentageTableEntry.setCommunityDepleted(
                communityDepleted
        );

        // Grid-Portion berechnen.
        // Gibt an, wie viel Prozent des gesamten Verbrauchs
        // aus dem öffentlichen Netz kommt.
        double totalUsed = receiveMessage.getCommunityUsed()
                + receiveMessage.getGridUsed();

        // Wenn noch keine Energie verbraucht wurde, wird 0 gesetzt,
        // damit keine Division durch 0 entsteht.
        double gridPortion = totalUsed > 0
                ? receiveMessage.getGridUsed() / totalUsed * 100
                : 0;

        currentPercentageTableEntry.setGridPortion(gridPortion);

        // Aktuellen Stand der Percentage-Tabelle in der Datenbank speichern.
        currentPercentageTableRepository.save(currentPercentageTableEntry);

        // Empfangene Nachricht in Konsole ausgeben.
        System.out.println(receiveMessage.printOutput());
    }
}