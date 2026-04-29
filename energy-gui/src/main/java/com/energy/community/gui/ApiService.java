package com.energy.community.gui;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ApiService {

    private static final String BASE_URL = "http://localhost:8083/energy";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CurrentResponse getCurrentData() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/current"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return objectMapper.readValue(response.body(), CurrentResponse.class);
    }

    public HistoricalResponse getHistoricalData(String startIso, String endIso) throws Exception {
        String url = BASE_URL + "/historical?start="
                + URLEncoder.encode(startIso, StandardCharsets.UTF_8)
                + "&end="
                + URLEncoder.encode(endIso, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return objectMapper.readValue(response.body(), HistoricalResponse.class);
    }
}