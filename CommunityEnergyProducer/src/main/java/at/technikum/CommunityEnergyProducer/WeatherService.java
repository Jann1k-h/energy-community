package at.technikum.CommunityEnergyProducer;

import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

@Service
public class WeatherService {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public int getCurrentCloudCover() throws Exception {
        String url = "https://api.open-meteo.com/v1/forecast"
                + "?latitude=48.239166"
                + "&longitude=16.377441"
                + "&hourly=cloud_cover"
                + "&timezone=Europe%2FBerlin"
                + "&forecast_days=1";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            throw new IllegalStateException("Weather API antwortet mit " + response.statusCode());
        }

        JsonNode root = objectMapper.readTree(response.body());
        int hour = LocalDateTime.now().getHour();

        return root.path("hourly")
                .path("cloud_cover")
                .get(hour)
                .asInt();
    }
}
