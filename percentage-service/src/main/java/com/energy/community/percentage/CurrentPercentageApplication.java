package com.energy.community.percentage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CurrentPercentageApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrentPercentageApplication.class, args);
    }
}