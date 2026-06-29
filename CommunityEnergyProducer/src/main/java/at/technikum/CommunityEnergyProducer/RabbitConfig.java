package at.technikum.CommunityEnergyProducer;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // Bedeutet: Klasse enthält Spring-Einstellungen: Spring erstellt die Dinge, die man mit @Bean definiert.
public class RabbitConfig {

    // Queue für Austausch zwischen den Producern und Consumern, also Community energyProducer/User und Usage Service
    public static final String ENERGY_QUEUE = "energy.queue";

    @Bean // Spring soll Objekt selbst erstellen und verwalten
    // Erstellt Queue mit namen energy.queue
    // durable = true: Queue bleibt bestehen, auch wenn RabbitMQ neu gestartet wird
    public Queue energyQueue() {
        return new Queue(ENERGY_QUEUE, true);
    }
}