package ru.otus.java.professional.yampolskiy.tttaskservice.tasks.exceptions.taskpriority;

import java.util.UUID;

public class TaskPriorityAlreadyDeletedException extends TaskPriorityException {
    public TaskPriorityAlreadyDeletedException(UUID id) {
        super("TaskPriority already deleted with id: " + id);
    }
}