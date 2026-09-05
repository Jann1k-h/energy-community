package at.technikum.UsageService;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

// Diese Klasse ist ausschließlich für das technische vrsenden von Usage-Updates an RabbitMQ zuständig.
@Service
public class UsageUpdatePublisher {

    private final RabbitTemplate rabbitTemplate;

    // Konstruktor-Injection:
    // Spring Boot übergibt das RabbitTemplate automatisch.
    public UsageUpdatePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // Sendet die fertige Usage-Update-Nachricht an die Queue,
    // aus der der Current Percentage Service sie empfängt.
    public void publish(SendMessage message) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.USAGE_UPDATE_QUEUE,
                message.toString()
        );
    }
}