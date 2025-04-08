package ru.otus.java.professional.yampolskiy.tttaskservice.tasks.controllers.taskpriority;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.java.professional.yampolskiy.tttaskservice.common.dto.ErrorDTO;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.exceptions.taskpriority.InvalidTaskPriorityException;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.exceptions.taskpriority.TaskPriorityAccessDeniedException;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.exceptions.taskpriority.TaskPriorityAlreadyDeletedException;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.exceptions.taskpriority.TaskPriorityNotFoundException;

@RestControllerAdvice(basePackages = "ru.otus.java.professional.yampolskiy.tttaskservice.tasks.controllers.taskpriority")
public class TaskPriorityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(TaskPriorityExceptionHandler.class);

    @ExceptionHandler(TaskPriorityNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFound(TaskPriorityNotFoundException ex) {
        return new ResponseEntity<>(new ErrorDTO("TASK_PRIORITY_NOT_FOUND", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidTaskPriorityException.class)
    public ResponseEntity<ErrorDTO> handleInvalid(InvalidTaskPriorityException ex) {
        return new ResponseEntity<>(new ErrorDTO("TASK_PRIORITY_INVALID", ex.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(TaskPriorityAlreadyDeletedException.class)
    public ResponseEntity<ErrorDTO> handleAlreadyDeleted(TaskPriorityAlreadyDeletedException ex) {
        return new ResponseEntity<>(new ErrorDTO("TASK_PRIORITY_ALREADY_DELETED", ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TaskPriorityAccessDeniedException.class)
    public ResponseEntity<ErrorDTO> handleCommentAccessDenied(TaskPriorityAccessDeniedException e) {
        return new ResponseEntity<>(new ErrorDTO("TASK_PRIORITY_ACCESS_DENIED", e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleInternal(Exception ex) {
        log.error("Ошибка в модуле TaskPriority", ex);
        return new ResponseEntity<>(new ErrorDTO("TASK_PRIORITY_INTERNAL_ERROR", "Внутренняя ошибка сервиса приоритетов задач"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
