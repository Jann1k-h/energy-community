package com.energy.community.community.producer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Klasse enthält Bean-Definitionen, die beim Start der Anwendung erzeugt werden sollen.
@Configuration
public class RabbitMQConfig {

    // --------------------------------------------------
    // Werte für Queue, Exchange und Routing Key werden aus der application.properties Datei geladen
    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;
    // --------------------------------------------------


    // Producer sendet Nachricht
    // Exchange empfängt Nachricht
    // Binding prüft Routing Key
    // Queue bekommt Nachricht
    // Consumer liest Nachricht

    // --------------------------------------------------
    // Exchange wird erstellt
    // Producer sendet Nachrichten an diesen Exchange
    // DirectExchange leitet Nachrichten anhand eines exakt passenden Routing Keys weiter
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchangeName);
    }
    // --------------------------------------------------

    // --------------------------------------------------
    // Queue wird erstellt
    // In dieser Queue werden Nachrichten gespeichert, bis ein Consumer sie abholt und verarbeitet
    // true bedeutet: Die Queue ist durable und bleibt nach einem RabbitMQ-Neustart bestehen.
    @Bean
    public Queue queue() {
        return new Queue(queueName, true);
    }
    // --------------------------------------------------

    // --------------------------------------------------
    // Binding wird erstellt
    // Verbindet den Exchange mit der Queue
    // Der Routing Key legt fest, welche Nachrichten vom Exchange in diese Queue gelangen.
    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(routingKey);
    }
    // --------------------------------------------------

    // --------------------------------------------------
    // Message Converter wird erstellt
    // wandelt Java-Objekte automatisch in JSON um, bevor sie als Nachricht an RabbitMQ gesendet werden
    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
    // --------------------------------------------------

    // --------------------------------------------------
    // RabbitTemplate wird erstellt
    // RabbitTemplate ist das zentrale Objekt, mit dem Producer Nachrichten an RabbitMQ sendet
    // ConnectionFactory stellt die Verbindung zu RabbitMQ bereit
    // MessageConverter sorgt dafür, dass Java-Objekte als JSON verschickt werden
    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            JacksonJsonMessageConverter messageConverter
    ) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
    // --------------------------------------------------
}