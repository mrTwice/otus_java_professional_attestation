package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);
    @RequestMapping("/error")
    public ResponseEntity<String> handleError(HttpServletRequest request) {
        LOGGER.info("Custom error: {}", request.getQueryString());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Custom error: " + request.getQueryString());
    }
}