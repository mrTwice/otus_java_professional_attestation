package ru.otus.java.professional.yampolskiy.tttaskservice.comments.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.java.professional.yampolskiy.tttaskservice.comments.exceptions.CommentAccessDeniedException;
import ru.otus.java.professional.yampolskiy.tttaskservice.comments.exceptions.CommentNotFoundException;
import ru.otus.java.professional.yampolskiy.tttaskservice.comments.exceptions.InvalidCommentException;
import ru.otus.java.professional.yampolskiy.tttaskservice.common.dto.ErrorDTO;

@RestControllerAdvice(basePackages = "ru.otus.java.professional.yampolskiy.tttaskservice.comments.controllers")
public class CommentExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentExceptionHandler.class);

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleCommentNotFound(CommentNotFoundException e) {
        return new ResponseEntity<>(new ErrorDTO("COMMENT_NOT_FOUND", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCommentException.class)
    public ResponseEntity<ErrorDTO> handleInvalidComment(InvalidCommentException e) {
        return new ResponseEntity<>(new ErrorDTO("COMMENT_INVALID", e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(CommentAccessDeniedException.class)
    public ResponseEntity<ErrorDTO> handleCommentAccessDenied(CommentAccessDeniedException e) {
        return new ResponseEntity<>(new ErrorDTO("COMMENT_ACCESS_DENIED", e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGeneric(Exception e) {
        LOGGER.error("Ошибка в модуле комментариев", e);
        return new ResponseEntity<>(new ErrorDTO("COMMENT_INTERNAL_ERROR", "Внутренняя ошибка сервиса комментариев"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
