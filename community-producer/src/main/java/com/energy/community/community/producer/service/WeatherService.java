package com.energy.community.community.producer.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class WeatherService {

    private String url = "https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&hourly=cloud_cover&past_days=0&forecast_days=1";

    public int getCloudCover() {

        // API anfrage an URL; zurückgeschickt wird JSON, was in WeatherResponse Obejct gespeichert wird
        // darin wird das Feld "hourly" befüllt, welches die cloud_cover-Daten enthält
        // Hourly ist ein array, in dem die Werte für jede Stunde des Tages gespeichert sind
        WeatherResponse response = new RestTemplate().getForObject(url, WeatherResponse.class);

        int currentHour = LocalDateTime.now().getHour();

        return response.hourly.cloud_cover[currentHour];
    }
}

class WeatherResponse {
    public Hourly hourly;
}

class Hourly {
    public int[] cloud_cover;
}