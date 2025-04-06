package ru.otus.java.professional.yampolskiy.tttaskservice.common;

public interface DomainValidator<T> {

    void validateForCreate(T entity);

    void validateForUpdate(T existingEntity, T updatedEntity);
}
