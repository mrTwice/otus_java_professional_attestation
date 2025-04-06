package ru.otus.java.professional.yampolskiy.tttaskservice.taskstatuses.validators;

import ru.otus.java.professional.yampolskiy.tttaskservice.common.DomainValidator;
import ru.otus.java.professional.yampolskiy.tttaskservice.taskstatuses.entities.TaskStatus;

public class TaskStatusValidator implements DomainValidator<TaskStatus> {

    @Override
    public void validateForCreate(TaskStatus status) {
        validateCommon(status);
    }

    @Override
    public void validateForUpdate(TaskStatus existing, TaskStatus updated) {
        validateCommon(updated);
        if (!existing.getCode().equals(updated.getCode())) {
            throw new IllegalArgumentException("TaskStatus code cannot be changed");
        }
    }

    private void validateCommon(TaskStatus status) {
        if (status.getCode() == null || status.getCode().isBlank()) {
            throw new IllegalArgumentException("Code is required");
        }
        if (status.getName() == null || status.getName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (status.getColor() != null && !status.getColor().matches("^#[0-9A-Fa-f]{6}$")) {
            throw new IllegalArgumentException("Color must be a hex value like #FF0000");
        }
    }
}
