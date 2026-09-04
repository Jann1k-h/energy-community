package at.technikum.CommunityEnergyProducer;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommunityEnergyProducer {

    private final WeatherService weatherService;
    private final EnergyMessagePublisher messagePublisher;

    public CommunityEnergyProducer(
            WeatherService weatherService,
            EnergyMessagePublisher messagePublisher
    ) {
        this.weatherService = weatherService;
        this.messagePublisher = messagePublisher;
    }

    @Scheduled(fixedDelayString = "${producer.send-delay-ms:3000}")
    public void sendMessage() throws Exception {
        SendMessage message = new SendMessage(
                "PRODUCER",
                "COMMUNITY",
                calculatedKwh(),
                LocalDateTime.now().toString()
        );

        messagePublisher.publish(message);

        System.out.println("Nachricht gesendet: " + message);
    }

    private double calculatedKwh() throws Exception {
        int hour = LocalDateTime.now().getHour();

        // Nachts produziert eine Solaranlage nichts.
        if (hour < 6 || hour > 20) {
            return 0.0;
        }

        int cloudCover = weatherService.getCurrentCloudCover();

        if (cloudCover <= 20) {
            return 0.0020 + Math.random() * 0.0020;
        } else if (cloudCover <= 50) {
            return 0.0012 + Math.random() * 0.0015;
        } else if (cloudCover <= 80) {
            return 0.0004 + Math.random() * 0.0010;
        }

        return 0.0001 + Math.random() * 0.0004;
    }
}