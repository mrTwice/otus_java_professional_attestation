package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.infrastructure.dto;


public class ValidationEmailPartErrorDTO {

    private String field;
    private String message;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ValidationEmailPartErrorDTO() {
    }

    public ValidationEmailPartErrorDTO(String field, String message) {
        this.field = field;
        this.message = message;
    }

}
