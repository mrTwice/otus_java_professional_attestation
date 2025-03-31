package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.integrations.exceptions;

public class IntegrationException extends RuntimeException {
    private final String code;

    public String getCode() {
        return code;
    }

    public IntegrationException(String message, String code) {
        super(message);
        this.code = code;
    }
}