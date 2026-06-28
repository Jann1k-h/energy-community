package at.technikum.RestApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// macht Klasse zur Spring-Boot-App:
// sucht automatisch nach Klassen mit @Service, @Configuration, @Repository, @Controller usw.
// erstellt alle benötigten Objekte
// konfiguriert Spring Boot automatisch
@SpringBootApplication
public class RestApiApplication {

    public static void main(String[] args) {

        // startet Spring:
        // startet Spring-Boot-Anwendung
        // ladet alle Spring-Komponenten
        // liest application.properties
        // startet RabbitMQ-Verbindung
        SpringApplication.run(RestApiApplication.class, args);
    }

}
