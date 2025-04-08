package ru.otus.java.professional.yampolskiy.tttaskservice.taskstatuses.exceptions;

public class TaskStatusAccessDeniedException extends TaskStatusException {
    public TaskStatusAccessDeniedException(String message) {
        super(message);
    }
}
