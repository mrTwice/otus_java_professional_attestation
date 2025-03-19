package ru.otus.java.professional.yampolskiy.ttuserservice.exceptions;

import ru.otus.java.professional.yampolskiy.ttuserservice.entities.ValidationEmailPartError;

import java.util.List;

public class ValidationEmailException extends RuntimeException {
    private String code;
    private List<ValidationEmailPartError> errors;

    public String getCode() {
        return code;
    }

    public List<ValidationEmailPartError> getErrors() {
        return errors;
    }

    public ValidationEmailException(String code, String message, List<ValidationEmailPartError> errors) {
        super(message);
        this.code = code;
        this.errors = errors;
    }
}
