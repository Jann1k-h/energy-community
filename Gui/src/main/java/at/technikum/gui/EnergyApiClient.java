package at.technikum.gui;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// Diese Klasse ist ausschließlich für die Kommunikation
// zwischen GUI und REST API zuständig.
public class EnergyApiClient {

    // Basis-Adresse der REST API.
    private static final String BASE_URL = "http://localhost:8084";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public EnergyApiClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    // Holt die Prozentwerte der aktuellen Stunde über die REST API.
    public CurrentPercentageDto getCurrentPercentage() throws Exception {
        JsonNode root = getJson("/energy/current");

        return new CurrentPercentageDto(
                root.path("communityDepleted").asDouble(),
                root.path("gridPortion").asDouble()
        );
    }

    // Holt die historischen Verbrauchsdaten für einen Zeitraum
    // über die REST API.
    public HistoricalUsageDto getHistoricalUsage(
            String start,
            String end
    ) throws Exception {
        String path = "/energy/historical?start=" + start + "&end=" + end;

        JsonNode root = getJson(path);

        return new HistoricalUsageDto(
                root.path("communityProduced").asDouble(),
                root.path("communityUsed").asDouble(),
                root.path("gridUsed").asDouble()
        );
    }

    // Führt eine GET-Anfrage aus und wandelt die JSON-Antwort
    // in einen JsonNode um.
    private JsonNode getJson(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        // Nur eine erfolgreiche Antwort darf verarbeitet werden.
        if (response.statusCode() != 200) {
            throw new IllegalStateException(
                    "REST API antwortet mit Status " + response.statusCode()
            );
        }

        return objectMapper.readTree(response.body());
    }
}