package ru.otus.java.professional.yampolskiy.tttaskservice.taskstatuses.exceptions;

import java.util.UUID;

public class TaskStatusNotFoundException extends TaskStatusException {
  public TaskStatusNotFoundException(String code) {
    super("TaskStatus not found by code: " + code);
  }
  public TaskStatusNotFoundException(UUID id) {
    super("TaskStatus not found by id: " + id);
  }
}
