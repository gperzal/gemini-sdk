package com.gperzal.gemini;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gperzal.gemini.exception.GeminiException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Cliente HTTP interno del SDK.
 * Gestiona toda la comunicación con la API REST de Gemini.
 */
class GeminiClient {

    private static final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/";
    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    GeminiClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.mapper = new ObjectMapper();
    }

    /**
     * Envía la solicitud HTTP a Gemini y devuelve el texto generado.
     */
    public String send(String model, String apiKey, GeminiRequest request) {
        try {
            String url = BASE_URL + model + ":generateContent?key=" + apiKey;
            String body = mapper.writeValueAsString(request.toBody());

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(30))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                GeminiResponse geminiResponse = mapper.readValue(response.body(), GeminiResponse.class);
                return geminiResponse.firstText();
            }

            throw new GeminiException("Gemini API error: " + response.statusCode() + " - " + response.body());

        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) Thread.currentThread().interrupt();
            throw new GeminiException("Error calling Gemini API: " + e.getMessage(), e);
        }
    }
}
