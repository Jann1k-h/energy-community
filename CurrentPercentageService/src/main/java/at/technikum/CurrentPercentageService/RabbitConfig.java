package at.technikum.CurrentPercentageService;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // Spring-Einstellungen
public class RabbitConfig {

    // Queue für Austausch zwischen UsageService und Current Percentage Service
    public static final String USAGE_UPDATE_QUEUE = "usage.update.queue";

    @Bean // Spring soll Objekt selbst erstellen und verwalten
    public Queue usageUpdateQueue() {
        return new Queue(USAGE_UPDATE_QUEUE, true);
    }

}