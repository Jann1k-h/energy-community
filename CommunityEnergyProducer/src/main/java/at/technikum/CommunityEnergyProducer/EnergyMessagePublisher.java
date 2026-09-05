package at.technikum.CommunityEnergyProducer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class EnergyMessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    public EnergyMessagePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(SendMessage message) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.ENERGY_QUEUE,
                message.toString()
        );
    }
}