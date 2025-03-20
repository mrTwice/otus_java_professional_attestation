package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.dtos;

import lombok.Data;

@Data
public class TokenValidationRequest {
    private String tokenValue;
}