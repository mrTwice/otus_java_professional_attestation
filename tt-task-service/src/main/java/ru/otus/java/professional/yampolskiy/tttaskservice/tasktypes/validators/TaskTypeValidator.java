package ru.otus.java.professional.yampolskiy.tttaskservice.tasktypes.validators;

import ru.otus.java.professional.yampolskiy.tttaskservice.common.DomainValidator;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasktypes.entities.TaskType;

public class TaskTypeValidator implements DomainValidator<TaskType> {

    @Override
    public void validateForCreate(TaskType type) {
        validateCommon(type);
    }

    @Override
    public void validateForUpdate(TaskType existing, TaskType updated) {
        validateCommon(updated);
        if (!existing.getCode().equals(updated.getCode())) {
            throw new IllegalArgumentException("TaskType code cannot be changed");
        }
    }

    private void validateCommon(TaskType type) {
        if (type.getCode() == null || type.getCode().isBlank()) {
            throw new IllegalArgumentException("Code is required");
        }
        if (type.getName() == null || type.getName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (type.getIcon() != null && type.getIcon().length() > 100) {
            throw new IllegalArgumentException("Icon must be 100 characters max");
        }
    }
}
