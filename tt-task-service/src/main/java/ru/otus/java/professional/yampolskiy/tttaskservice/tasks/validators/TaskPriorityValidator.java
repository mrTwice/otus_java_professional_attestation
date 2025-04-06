package ru.otus.java.professional.yampolskiy.tttaskservice.tasks.validators;


import ru.otus.java.professional.yampolskiy.tttaskservice.common.DomainValidator;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.entities.TaskPriority;

import java.util.regex.Pattern;

public class TaskPriorityValidator implements DomainValidator<TaskPriority> {

    private static final Pattern HEX_COLOR = Pattern.compile("^#[0-9A-Fa-f]{6}$");

    @Override
    public void validateForCreate(TaskPriority priority) {
        validateRequired(priority);
        validateColor(priority.getColor());
        validateSortOrder(priority.getSortOrder());
    }

    @Override
    public void validateForUpdate(TaskPriority existing, TaskPriority updated) {
        if (!existing.getCode().equals(updated.getCode())) {
            throw new IllegalArgumentException("TaskPriority code cannot be changed");
        }
        validateRequired(updated);
        validateColor(updated.getColor());
        validateSortOrder(updated.getSortOrder());
    }

    private void validateRequired(TaskPriority p) {
        if (p.getCode() == null || p.getCode().isBlank()) {
            throw new IllegalArgumentException("Code is required");
        }
        if (p.getName() == null || p.getName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
    }

    private void validateColor(String color) {
        if (color != null && !HEX_COLOR.matcher(color).matches()) {
            throw new IllegalArgumentException("Color must be in format #RRGGBB");
        }
    }

    private void validateSortOrder(Integer sortOrder) {
        if (sortOrder != null && sortOrder < 0) {
            throw new IllegalArgumentException("Sort order must be >= 0");
        }
    }
}
