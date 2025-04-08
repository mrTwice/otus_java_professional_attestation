package ru.otus.java.professional.yampolskiy.tttaskservice.attachments.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.java.professional.yampolskiy.tttaskservice.attachments.exceptions.AttachmentAccessDeniedException;
import ru.otus.java.professional.yampolskiy.tttaskservice.attachments.exceptions.AttachmentNotFoundException;
import ru.otus.java.professional.yampolskiy.tttaskservice.attachments.exceptions.InvalidAttachmentException;
import ru.otus.java.professional.yampolskiy.tttaskservice.common.dto.ErrorDTO;

@RestControllerAdvice(basePackages = "ru.otus.java.professional.yampolskiy.tttaskservice.attachments.controllers")
public class AttachmentExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttachmentExceptionHandler.class);

    @ExceptionHandler(AttachmentNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleAttachmentNotFoundException(AttachmentNotFoundException e) {
        return new ResponseEntity<>(new ErrorDTO("ATTACHMENT_NOT_FOUND", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidAttachmentException.class)
    public ResponseEntity<ErrorDTO> handleInvalidAttachmentException(InvalidAttachmentException e) {
        return new ResponseEntity<>(new ErrorDTO("ATTACHMENT_INVALID", e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(AttachmentAccessDeniedException.class)
    public ResponseEntity<ErrorDTO> handleAttachmentAccessDeniedException(AttachmentAccessDeniedException e) {
        return new ResponseEntity<>(new ErrorDTO("ATTACHMENT_ACCESS_DENIED", e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGenericException(Exception e) {
        LOGGER.error("Ошибка в модуле вложений", e);
        return new ResponseEntity<>(new ErrorDTO("ATTACHMENT_INTERNAL_ERROR", "Внутренняя ошибка сервиса вложений"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

