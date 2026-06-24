package at.technikum.CommunityEnergyUser;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // Spring-Einstellungen
public class RabbitConfig {

    // Queue für Austausch zwischen den Producern und Consumern, also Community energyProducer/User und Usage Service
    public static final String ENERGY_QUEUE = "energy.queue";

    @Bean // Spring soll Objekt selbst erstellen und verwalten
    public Queue energyQueue() {
        return new Queue(ENERGY_QUEUE, true);
    }
}