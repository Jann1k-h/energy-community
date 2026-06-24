package at.technikum.UsageService;

import at.technikum.UsageService.db.HourlyUsageTable;
import at.technikum.UsageService.db.HourlyUsageTableRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service // Spring-Klasse mit Logik
public class UsageService {

    // Objekt deklarieren
    private final RabbitTemplate rabbitTemplate;

    // Objekt deklarieren, ohne dem kann UsageService nicht auf hourlyUsageTableRepository zugreifen und befehle wie .findById .save,... ausführen
    private HourlyUsageTableRepository hourlyUsageTableRepository;

    // 1 Konstruktor holt sich von Spring RabbitTemplate, damit Producer Nachrichten senden kann
    // 2 Konstruktor gibt UsageService das hourlyUsageTablRepository
    // 2 Da das Repository ein Interface ist, kann man nicht in der Zeile drüber = new ... machen, sondern löst es über den Konstruktor
    public UsageService(RabbitTemplate rabbitTemplate, HourlyUsageTableRepository hourlyUsageTableRepository) {
        /* 1 */ this.rabbitTemplate = rabbitTemplate;
        /* 2 */ this.hourlyUsageTableRepository = hourlyUsageTableRepository;
    }

    @RabbitListener(queues = RabbitConfig.ENERGY_QUEUE)
    public void receiveMessage(String message) {

        ReceiveMessage receiveMessage = new ReceiveMessage(message);

        // Obejtktinitialisierung
        HourlyUsageTable hourlyUsageTableEntry;

        if (hourlyUsageTableRepository.findById(receiveMessage.getDatetime()).isPresent()) {
            // existiert schon → aus DB holen
            hourlyUsageTableEntry = hourlyUsageTableRepository
                    .findById(receiveMessage.getDatetime())
                    .get();
        } else {
            // existiert nicht → neues Objekt erstellen
            hourlyUsageTableEntry = new HourlyUsageTable();
            hourlyUsageTableEntry.setHour(receiveMessage.getDatetime());
        }

        if(receiveMessage.getType().equals("PRODUCER")) {
            hourlyUsageTableEntry.setCommunityProduced(
                    hourlyUsageTableEntry.getCommunityProduced() + receiveMessage.getKwh()
            );
        }

        if(receiveMessage.getType().equals("USER")) {

            // Wenn Community Used <= Community Produced ist, kann man einfach zu Community Used dazuaddieren
            if(hourlyUsageTableEntry.getCommunityUsed() + receiveMessage.getKwh() <= hourlyUsageTableEntry.getCommunityProduced()) {

                hourlyUsageTableEntry.setCommunityUsed(
                        hourlyUsageTableEntry.getCommunityUsed() + receiveMessage.getKwh()
                );

            // Wenn Community Used > Community Produced ist, berechnet man Differenz von (COmmunity Used - Community Produced) und der neuen Nachricht und addiert es zum Grid
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

        hourlyUsageTableRepository.save(hourlyUsageTableEntry);

        System.out.println(receiveMessage.printOutput());


        // Message versenden
        SendMessage messageToCurrentPercentageService = new SendMessage(
                "CurrentPercentageService",
                receiveMessage.getDatetime().toString()
        );

        // RabbitMQ kann kein Java Objekt verstehen, deswegen als String versenden toString() in jeweiligen SendMessage Klasse festgelegt
        String stringMessage = messageToCurrentPercentageService.toString();

        rabbitTemplate.convertAndSend(RabbitConfig.USAGE_UPDATE_QUEUE, stringMessage);

        System.out.println("Nachricht gesendet: " + stringMessage);

    }
}


