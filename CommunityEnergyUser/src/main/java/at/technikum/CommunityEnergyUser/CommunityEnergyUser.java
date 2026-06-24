package at.technikum.CommunityEnergyUser;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service // Spring-Klasse mit Logik
public class CommunityEnergyUser {

    private final RabbitTemplate rabbitTemplate;

    // Constructor holt sich von Spring RabbitTemplate, damit Producer Nachrichten senden kann
    public CommunityEnergyUser(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedRate = 3000)
    public void sendMessage() {

                SendMessage message = new SendMessage(
                "USER",
                "COMMUNITY",
                calculatedKwh(),
                LocalDateTime.now().toString()
        );

        // RabbitMQ kann kein Java Objekt verstehen, deswegen als String versenden toString() in jeweiligen SendMessage Klasse festgelegt
        String stringMessage = message.toString();

        rabbitTemplate.convertAndSend(RabbitConfig.ENERGY_QUEUE, stringMessage);

        System.out.println("Nachricht gesendet: " + stringMessage);
    }

    // Verbrauch bestimmen
    private double calculatedKwh() {

        int hour = LocalDateTime.now().getHour();

        double kwh;

        // Morgen Verbrauch höher
        if (hour >= 6 && hour <= 9) {
            kwh = 0.0008 + Math.random() * 0.0012;   // 0.0008 bis 0.0020 kWh

        // Abend höchster Verbrauch
        } else if (hour >= 17 && hour <= 22) {
            kwh = 0.0012 + Math.random() * 0.0020;   // 0.0012 bis 0.0032 kWh

        // Nacht sehr niedriger Verbrauch
        } else if (hour >= 0 && hour <= 5) {
            kwh = 0.0001 + Math.random() * 0.0004;   // 0.0001 bis 0.0005 kWh

        // ansonsten normaler Verbrauch
        } else {
            kwh = 0.0004 + Math.random() * 0.0008;   // 0.0004 bis 0.0012 kWh
        }

        return kwh;
    }
}