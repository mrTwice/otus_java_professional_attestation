package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.dtos;

import lombok.Data;

@Data
public class TokenRequest {
    private String clientId;
    private String clientSecret;
    private String grantType;
    private String username;
    private String password;
    private String scope;
}