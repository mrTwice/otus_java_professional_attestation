package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.dtos;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.Set;

@Getter
@Builder
public class OAuth2TokenIntrospectionResponse {
    private boolean active; // true - если токен действителен, false - если нет
    private String clientId;
    private Set<String> scopes;
    private Instant expiresAt;
}
