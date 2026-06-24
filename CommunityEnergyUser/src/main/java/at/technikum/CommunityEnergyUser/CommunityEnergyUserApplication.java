package at.technikum.CommunityEnergyUser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CommunityEnergyUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunityEnergyUserApplication.class, args);
    }

}