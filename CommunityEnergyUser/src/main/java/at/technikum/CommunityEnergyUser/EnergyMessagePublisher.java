
package at.technikum.CommunityEnergyUser;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

// Diese Klasse ist ausschließlich für das technische versenden von Nachrichten an RabbitMQ zuständig.
@Service
public class EnergyMessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    // Konstruktor-Injection:
    // Spring Boot übergibt das RabbitTemplate automatisch.
    public EnergyMessagePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // Sendet eine fertige Energienachricht an die energy.queue.
    public void publish(SendMessage message) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.ENERGY_QUEUE,
                message.toString()
        );
    }
}