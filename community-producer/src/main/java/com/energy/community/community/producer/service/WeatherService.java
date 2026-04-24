package com.energy.community.community.producer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class WeatherService {

    @Value("${weather.api.url}")
    private String weatherApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public int getCloudCover() {
        try {
            Map response = restTemplate.getForObject(weatherApiUrl, Map.class);

            Map current = (Map) response.get("current");

            Number cloudCover = (Number) current.get("cloud_cover");

            return cloudCover.intValue();
        } catch (Exception e) {
            System.out.println("Wetter API Fehler. Verwende Standardwert 50% Bewölkung.");
            return 50;
        }
    }
}