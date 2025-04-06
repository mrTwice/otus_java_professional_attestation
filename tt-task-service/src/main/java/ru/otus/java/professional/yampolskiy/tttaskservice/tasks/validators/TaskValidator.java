package ru.otus.java.professional.yampolskiy.tttaskservice.tasks.validators;

import ru.otus.java.professional.yampolskiy.tttaskservice.common.DomainValidator;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.entities.Task;

import java.time.Instant;

public class TaskValidator implements DomainValidator<Task> {

    @Override
    public void validateForCreate(Task task) {
        validateRequiredFields(task);
        validateDueDate(task.getDueDate(), Instant.now());
        validateSelfParent(task);
    }

    @Override
    public void validateForUpdate(Task existingTask, Task updatedTask) {
        if (existingTask.getCompletedAt() != null) {
            throw new IllegalStateException("Cannot update a completed task");
        }

        validateRequiredFields(updatedTask);
        validateDueDate(updatedTask.getDueDate(), existingTask.getCreatedAt());
        validateSelfParent(updatedTask);
    }

    private void validateRequiredFields(Task task) {
        if (task.getTitle() == null || task.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (task.getStatusCode() == null || task.getStatusCode().isBlank()) {
            throw new IllegalArgumentException("Status code is required");
        }
        if (task.getStatusId() == null) {
            throw new IllegalArgumentException("Status ID is required");
        }
        if (task.getTypeCode() == null || task.getTypeCode().isBlank()) {
            throw new IllegalArgumentException("Type code is required");
        }
        if (task.getTypeId() == null) {
            throw new IllegalArgumentException("Type ID is required");
        }
    }

    private void validateDueDate(Instant dueDate, Instant referenceDate) {
        if (dueDate != null && referenceDate != null && dueDate.isBefore(referenceDate)) {
            throw new IllegalArgumentException("Due date cannot be before creation time");
        }
    }

    private void validateSelfParent(Task task) {
        if (task.getParent() != null && task.getId() != null &&
                task.getParent().getId().equals(task.getId())) {
            throw new IllegalArgumentException("Task cannot be its own parent");
        }
    }
}


