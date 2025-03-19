package ru.otus.java.professional.yampolskiy.ttuserservice.entities;

public class ValidationEmailPartError {
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

    public ValidationEmailPartError() {
    }

    public ValidationEmailPartError(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
