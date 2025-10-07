package com.gperzal.gemini;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Representa la respuesta JSON del modelo Gemini.
 * Usa JsonNode para manejar estructuras dinámicas sin generar warnings.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unused")
public class GeminiResponse {

    private JsonNode candidates;

    public String firstText() {
        if (candidates == null || !candidates.isArray() || candidates.isEmpty())
            return "No candidates found.";

        return candidates.get(0)
                .path("content")
                .path("parts")
                .path(0)
                .path("text")
                .asText("No text available");
    }

    // Getters y setters necesarios para la deserialización de Jackson
    public JsonNode getCandidates() { return candidates; }
    public void setCandidates(JsonNode candidates) { this.candidates = candidates; }
}
