package ru.otus.java.professional.yampolskiy.ttuserservice.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
