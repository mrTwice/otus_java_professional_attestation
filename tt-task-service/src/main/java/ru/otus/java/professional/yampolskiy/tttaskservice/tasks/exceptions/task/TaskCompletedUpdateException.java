package ru.otus.java.professional.yampolskiy.tttaskservice.tasks.exceptions.task;

import java.util.UUID;

public class TaskCompletedUpdateException extends TaskException {
    public TaskCompletedUpdateException(UUID id) {
        super("Cannot update task (already completed): " + id);
    }
}
