package at.technikum.UsageService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

// macht Klasse zur Spring-Boot-App:
// sucht automatisch nach Klassen mit @Service, @Configuration, @Repository, @Controller usw.
// erstellt alle benötigten Objekte
// konfiguriert Spring Boot automatisch
@SpringBootApplication

// aktiviert automatische Timer-Methoden wie @Scheduled.
@EnableScheduling
public class UsageServiceApplication {

    public static void main(String[] args) {

        // startet Spring-Boot-Anwendung:
        // ladet alle Spring-Komponenten
        // liest application.properties
        // startet RabbitMQ-Verbindung
        SpringApplication.run(UsageServiceApplication.class, args);

    }
}