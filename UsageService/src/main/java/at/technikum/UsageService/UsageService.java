package at.technikum.UsageService;

import at.technikum.UsageService.db.HourlyUsageTable;
import at.technikum.UsageService.db.HourlyUsageTableRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service // Spring-Klasse mit Logik
public class UsageService {

    // Feld für Objekt RabbitTemplate deklarieren
    // ohne dem kann UsageService nicht auf rabbitTemplate zugreifen und Befehle wie convertAndSend ausführen
    private final RabbitTemplate rabbitTemplate;

    // Feld für Objekt HourlyUsageTableRepository deklarieren,
    // ohne dem kann UsageService nicht auf hourlyUsageTableRepository zugreifen und Befehle wie .findById .save,... ausführen
    private final HourlyUsageTableRepository hourlyUsageTableRepository;

    // --------------------------------------------------
    // Konstruktor-Injection
    //
    // Injection:
    // Klasse erstellt benötigten Objekte nicht selbst mit "new",
    // sondern bekommt sie von Spring Boot übergeben
    // --> Objekte sind bereits richtig erstellt, konfiguriert und verwaltet
    //
    // 1 UsageService braucht RabbitTemplate zum Versenden von Nachrichten
    // 1 Spring Boot erstellt RabbitTemplate automatisch.
    // 1 Spring Boot übergibt es in den Konstruktor.
    // 1 Die Klasse speichert es in ihrer Variable.

    // 2 UsageService braucht HourlyUsageTableRepository zum Zugriff auf die Datenbank
    // 2 Spring Boot erstellt die Repository-Implementierung automatisch.
    // 2 Spring Boot übergibt sie in den Konstruktor.
    // 2 Die Klasse speichert sie in ihrer Variable.
    public UsageService(RabbitTemplate rabbitTemplate, HourlyUsageTableRepository hourlyUsageTableRepository) {
        /* 1 */ this.rabbitTemplate = rabbitTemplate;
        /* 2 */ this.hourlyUsageTableRepository = hourlyUsageTableRepository;
    }
    // --------------------------------------------------


    @RabbitListener(queues = RabbitConfig.ENERGY_QUEUE)
    // Wenn in Queue Nachricht ankommt, wird Methode angerufen und inhalt als Parameter in Methode übergeben
    public void receiveMessage(String message) {

        // String message wird in receiveMessage-Objekt umgewandelt
        ReceiveMessage receiveMessage = new ReceiveMessage(message);

        // Feld für Objekt HourlyUsageTable deklarieren
        HourlyUsageTable hourlyUsageTableEntry;

        // Wenn in Tabelle Eintrag mit aktuellen Stunde existiert, wird Zeit geholt
        if (hourlyUsageTableRepository.findById(receiveMessage.getDatetime()).isPresent()) {
            hourlyUsageTableEntry = hourlyUsageTableRepository
                    .findById(receiveMessage.getDatetime())
                    .get();
        // Ansonsten wird neuer Eintrag in Tabelle erzeugt mit der Zeit aus der Nachricht
        } else {
            // Java-Objekt erstellen für neue Tabellenzeile
            hourlyUsageTableEntry = new HourlyUsageTable();
            // In dem neuen Eintrag Stunde setzen
            hourlyUsageTableEntry.setHour(receiveMessage.getDatetime());
        }

        // Wenn Nachricht vom Typ Producer ist, wird es zum CommunityProduced dazugezählt
        if(receiveMessage.getType().equals("PRODUCER")) {
            hourlyUsageTableEntry.setCommunityProduced(
                    hourlyUsageTableEntry.getCommunityProduced() + receiveMessage.getKwh()
            );
        }

        // Wenn Nachricht vom Typ User ist, wird es zum CommunityUsed dazugezählt
        if(receiveMessage.getType().equals("USER")) {

            // Wenn Community Used + receiveMessage <= Community Produced ist, kann man einfach zu Community Used dazu addieren
            if(hourlyUsageTableEntry.getCommunityUsed() + receiveMessage.getKwh() <= hourlyUsageTableEntry.getCommunityProduced()) {

                hourlyUsageTableEntry.setCommunityUsed(
                        hourlyUsageTableEntry.getCommunityUsed() + receiveMessage.getKwh()
                );

            // Wenn Community Used > Community Produced ist, berechnet man Differenz von (Community Used - Community Produced) und der neuen Nachricht und addiert es zum Grid
            // Danach wird noch Community Used = Community Produced gesetzt
            } else {

                hourlyUsageTableEntry.setGridUsed(
                        hourlyUsageTableEntry.getGridUsed() + (hourlyUsageTableEntry.getCommunityUsed() - hourlyUsageTableEntry.getCommunityProduced() +  receiveMessage.getKwh())
                );

                hourlyUsageTableEntry.setCommunityUsed(
                        hourlyUsageTableEntry.getCommunityProduced()
                );

            }
        }

        // aktuellen Stand von hourlyUsageTableEntry in Datenbank speichern
        hourlyUsageTableRepository.save(hourlyUsageTableEntry);

        // Empfangene Nachricht in Konsole ausgeben
        System.out.println(receiveMessage.printOutput());

        // Message-Objekt erstellen
        SendMessage messageToCurrentPercentageService = new SendMessage(
                "CurrentPercentageService",
                receiveMessage.getDatetime().toString()
        );

        // Nachricht wird als String versendet
        // toString() ist in SendMessage-Klasse festgelegt
        String stringMessage = messageToCurrentPercentageService.toString();

        // Sendet stringMessage an die Queue energy.queue
        rabbitTemplate.convertAndSend(RabbitConfig.USAGE_UPDATE_QUEUE, stringMessage);

        // Ausgabe in Konsole
        System.out.println("Nachricht gesendet: " + stringMessage);
    }
}


