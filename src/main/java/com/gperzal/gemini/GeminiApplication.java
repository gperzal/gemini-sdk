package com.gperzal.gemini;

public class GeminiApplication {
    public static void main(String[] args) {
        GoogleGenerativeAI ai = GoogleGenerativeAI.connect();

        // 1️⃣ Prompt simple
        System.out.println(ai.prompt("Explícame cómo funciona el aprendizaje automático."));

        // 2️⃣ Prompt con archivo
        //File file = new File("D:\\Descargas\\data\\phones.csv");
        // System.out.println(ai.prompt("creame una tabla con las mejores caracteristica de los telefonos android", file));

        // 3️⃣ Prompt con lista de dato
        // List<String> ventas = List.of("Juan - Región Norte - $12000", "María - Región Sur - $18000");
        // System.out.println(ai.prompt("Top vendedores del mes:", ventas));
    }
}
