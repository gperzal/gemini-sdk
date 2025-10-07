package com.gperzal.gemini.exception;

/**
 * Excepción personalizada del SDK FactorAI Gemini.
 */
public class GeminiException extends RuntimeException {

    public GeminiException(String message) {
        super(message);
    }

    public GeminiException(String message, Throwable cause) {
        super(message, cause);
    }
}
