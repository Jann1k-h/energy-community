package at.technikum.CurrentPercentageService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CurrentPercentageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrentPercentageServiceApplication.class, args);
    }

}
