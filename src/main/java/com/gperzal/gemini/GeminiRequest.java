package com.gperzal.gemini;

import java.util.List;
import java.util.Map;

/**
 * Representa la estructura esperada por la API de Gemini.
 */
public record GeminiRequest(String prompt) {

    public Map<String, Object> toBody() {
        return Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(Map.of("text", prompt)))
                )
        );
    }
}
