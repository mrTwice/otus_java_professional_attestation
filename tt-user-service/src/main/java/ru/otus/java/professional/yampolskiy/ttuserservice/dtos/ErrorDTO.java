package ru.otus.java.professional.yampolskiy.ttuserservice.dtos;

import java.time.LocalDateTime;

public class ErrorDTO {
    private String code;
    private String message;
    private LocalDateTime dateTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public ErrorDTO(String code, String message) {
        this.code = code;
        this.message = message;
        this.dateTime = LocalDateTime.now();
    }
}
