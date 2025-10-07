package com.gperzal.gemini;

import com.gperzal.gemini.exception.GeminiException;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

/**
 * SDK principal de FactorAI para interactuar con Gemini.
 * Soporta prompts simples, con archivos o con datos estructurados.
 */
public class GoogleGenerativeAI {

    private final String apiKey;
    private final String model;
    private final GeminiClient client;

    // Constructor
    public GoogleGenerativeAI(String apiKey, String model) {
        if (apiKey == null || apiKey.isBlank())
            throw new GeminiException("Gemini API key is required.");
        if (model == null || model.isBlank())
            throw new GeminiException("Gemini model name is required.");
        this.apiKey = apiKey;
        this.model = model;
        this.client = new GeminiClient();
    }

    /**
     * Conecta automáticamente usando variables de entorno o .env
     */
    public static GoogleGenerativeAI connect() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        String apiKey = dotenv.get("GEMINI_API_KEY", System.getenv("GEMINI_API_KEY"));
        String model = dotenv.get("GEMINI_MODEL", System.getenv("GEMINI_MODEL"));
        return new GoogleGenerativeAI(apiKey, model);
    }

    // ============================================================
    // API FLUIDA
    // ============================================================

    // 1️⃣ Prompt simple
    public String prompt(String message) {
        return generate(message);
    }

    // 2️⃣ Prompt + archivo
    public String prompt(String message, File file) {
        return generate(message, file);
    }

    // 3️⃣ Prompt + lista de datos
    public String prompt(String message, List<?> data) {
        return generate(message, data);
    }

    // ============================================================
    // IMPLEMENTACIÓN BASE (generate)
    // ============================================================

    public String generate(String prompt) {
        GeminiRequest request = new GeminiRequest(prompt);
        return client.send(model, apiKey, request);
    }

    public String generate(String prompt, File file) {
        try {
            if (file == null || !file.exists() || file.length() == 0)
                return generate(prompt);

            String raw = Files.readString(file.toPath());
            String table = asMarkdownTable(raw);

            String fullPrompt = """
            A continuación se presentan los datos en formato de tabla:

            %s

            Basándote únicamente en esta información,
            responde a la siguiente pregunta:
            %s
            """.formatted(table, prompt);

            return client.send(model, apiKey, new GeminiRequest(fullPrompt));

        } catch (Exception e) {
            throw new GeminiException("Error reading file: " + e.getMessage(), e);
        }
    }


    public String generate(String prompt, List<?> data) {
        if (data == null || data.isEmpty())
            return generate(prompt);

        StringBuilder sb = new StringBuilder(prompt + "\n\nDatos cargados:\n");
        data.forEach(d -> sb.append("• ").append(d).append("\n"));

        return client.send(model, apiKey, new GeminiRequest(sb.toString()));
    }

    public String getModel() { return model; }
    public String getApiKey() { return apiKey; }

    private String asMarkdownTable(String csv) {
        String[] lines = csv.split("\\R");
        if (lines.length == 0) return csv;

        StringBuilder table = new StringBuilder("\n| ");
        table.append(String.join(" | ", lines[0].split(","))).append(" |\n");

        table.append("|").append("---|".repeat(lines[0].split(",").length)).append("\n");

        for (int i = 1; i < lines.length; i++) {
            table.append("| ")
                    .append(String.join(" | ", lines[i].split(",")))
                    .append(" |\n");
        }

        return table.toString();
    }

}
