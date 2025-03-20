package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.dtos;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String clientId;
    private String clientSecret;
    private String refreshToken;
}