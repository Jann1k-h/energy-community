package at.technikum.CurrentPercentageService;

import at.technikum.CurrentPercentageService.db.CurrentPercentageTable;
import at.technikum.CurrentPercentageService.db.CurrentPercentageTableRepository;
import at.technikum.CurrentPercentageService.db.HourlyUsageTable;
import at.technikum.CurrentPercentageService.db.HourlyUsageTableRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service // Spring-Klasse mit Logik
public class CurrentPercentageService {

    // Feld für Objekt CurrentPercentageTableRepository deklarieren,
    // ohne dem kann CurrentPercantageService nicht auf hourlyUsageTableRepository zugreifen und befehle wie .findById .save,... ausführen
    private final CurrentPercentageTableRepository currentPercentageTableRepository;

    // Feld für Objekt HourlyUsageTableRepository deklarieren,
    // ohne dem kann CurrentPercantageService nicht auf hourlyUsageTableRepository zugreifen und befehle wie .findById .save,... ausführen
    private final HourlyUsageTableRepository hourlyUsageTableRepository;

    // --------------------------------------------------
    // Konstruktor-Injection
    // CurrentPercantageService braucht currentPercentageTableRepository / HourlyUsageTableRepository zum Zugriff auf die Datenbank
    // Spring Boot erstellt die Repository-Implementierung automatisch.
    // Spring Boot übergibt sie in den Konstruktor.
    // Die Klasse speichert sie in ihrer Variable.
    public CurrentPercentageService(
            CurrentPercentageTableRepository currentPercentageTableRepository,
            HourlyUsageTableRepository hourlyUsageTableRepository
    ) {
        this.currentPercentageTableRepository = currentPercentageTableRepository;
        this.hourlyUsageTableRepository = hourlyUsageTableRepository;
    }
    // --------------------------------------------------

    @RabbitListener(queues = RabbitConfig.USAGE_UPDATE_QUEUE)
    // Wenn in Queue Nachricht ankommt, wird Methode angerufen und inhalt als Parameter in Methode übergeben
    public void receiveMessage(String message) {

        // String message wird in receiveMessage-Objekt umgewandelt
        ReceiveMessage receiveMessage = new ReceiveMessage(message);

        // Feld für Objekt CurrentPercentageTable deklarieren
        CurrentPercentageTable currentPercentageTableEntry;

        // Objektinitialisierung, um später Werte aus HourlyUsageTable holen zu können
        HourlyUsageTable hourlyUsageTableEntry = hourlyUsageTableRepository
                .findById(receiveMessage.getDatetime())
                .orElse(null);

        // Wenn in Tabelle Eintrag mit aktuellen Stunde existiert, wird Zeit geholt
        if (currentPercentageTableRepository.findById(receiveMessage.getDatetime()).isPresent()) {

            currentPercentageTableEntry = currentPercentageTableRepository
                    .findById(receiveMessage.getDatetime())
                    .get();

        // Ansonsten wird neuer Eintrag in Tabelle erzeugt mit der Zeit aus der Nachricht
        } else {
            // Java-Objekt erstellen für neue Tabellenzeile
            currentPercentageTableEntry = new CurrentPercentageTable();
            // In dem neuen Eintrag Stunde setzen
            currentPercentageTableEntry.setHour(receiveMessage.getDatetime());
        }

        // Community-Depleted berechnen
        // Gibt an, wie viel Prozent der produzierten Community-Energie bereits verbraucht wurde
        currentPercentageTableEntry.setCommunityDepleted(
                hourlyUsageTableEntry.getCommunityUsed() / hourlyUsageTableEntry.getCommunityProduced() * 100
        );

        // Grid-Portion berechnen
        // Gibt an, wie viel Prozent des gesamten Verbrauchs aus dem öffentlichen Netz kommt
        currentPercentageTableEntry.setGridPortion(
                hourlyUsageTableEntry.getGridUsed() / (hourlyUsageTableEntry.getCommunityUsed() + hourlyUsageTableEntry.getGridUsed()) * 100
        );

        // aktuellen Stand von hourlyUsageTableEntry in Datenbank speichern
        currentPercentageTableRepository.save(currentPercentageTableEntry);

    }

}


