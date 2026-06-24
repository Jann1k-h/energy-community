package at.technikum.CurrentPercentageService;

import at.technikum.CurrentPercentageService.db.CurrentPercentageTable;
import at.technikum.CurrentPercentageService.db.CurrentPercentageTableRepository;
import at.technikum.CurrentPercentageService.db.HourlyUsageTable;
import at.technikum.CurrentPercentageService.db.HourlyUsageTableRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service // Spring-Klasse mit Logik
public class CurrentPercentageService {

    // Objekt deklarieren, ohne dem kann CurrentPercentageService nicht auf currentPercentageTableRepository zugreifen und befehle wie .findById .save,... ausführen
    private CurrentPercentageTableRepository currentPercentageTableRepository;

    // Objekt deklarieren, ohne dem kann CurrentpercentageService nicht auf hourlyUsageTableRepository zugreifen und befehle wie .findById .save,... ausführen
    private HourlyUsageTableRepository hourlyUsageTableRepository;

    // Konstruktor gibt CurrentPercentageService beide Repositories
    // Da die Repositories Interfaces sind, kann man nicht in der Zeile drüber = new ... machen, sondern löst es über den Konstruktor
    public CurrentPercentageService(
            CurrentPercentageTableRepository currentPercentageTableRepository,
            HourlyUsageTableRepository hourlyUsageTableRepository
    ) {
        this.currentPercentageTableRepository = currentPercentageTableRepository;
        this.hourlyUsageTableRepository = hourlyUsageTableRepository;
    }

    @RabbitListener(queues = RabbitConfig.USAGE_UPDATE_QUEUE)
    public void receiveMessage(String message) {

        ReceiveMessage receiveMessage = new ReceiveMessage(message);

        // Objektdelarierung
        CurrentPercentageTable currentPercentageTableEntry;

        // Objektinitialisierung
        HourlyUsageTable hourlyUsageTableEntry = hourlyUsageTableRepository
                .findById(receiveMessage.getDatetime())
                .orElse(null);

        if (currentPercentageTableRepository.findById(receiveMessage.getDatetime()).isPresent()) {
            // existiert schon → aus DB holen
            currentPercentageTableEntry = currentPercentageTableRepository
                    .findById(receiveMessage.getDatetime())
                    .get();
        } else {
            // existiert nicht → neues Objekt erstellen
            currentPercentageTableEntry = new CurrentPercentageTable();
            currentPercentageTableEntry.setHour(receiveMessage.getDatetime());
        }

        currentPercentageTableEntry.setCommunityDepleted(hourlyUsageTableEntry.getCommunityUsed() / hourlyUsageTableEntry.getCommunityProduced() * 100);

        currentPercentageTableEntry.setGridPortion(hourlyUsageTableEntry.getGridUsed() / (hourlyUsageTableEntry.getCommunityUsed() + hourlyUsageTableEntry.getGridUsed()) * 100);

        currentPercentageTableRepository.save(currentPercentageTableEntry);



    }

}


