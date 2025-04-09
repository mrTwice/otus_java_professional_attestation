package ru.otus.java.professional.yampolskiy.tttaskservice.taskstatuses.exceptions;

public class TaskStatusAccessDeniedException extends TaskStatusException {
    public TaskStatusAccessDeniedException() {
        super("Access denied to task status resource.");
    }
}
