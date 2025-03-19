package ru.otus.java.professional.yampolskiy.ttuserservice.validators;

public interface Validator<T> {
    void validate(T object);
}
