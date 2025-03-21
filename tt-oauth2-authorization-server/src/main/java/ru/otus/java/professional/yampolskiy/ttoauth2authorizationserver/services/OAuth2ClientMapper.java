package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.entities.OAuth2ClientEntity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OAuth2ClientMapper {

    private final ObjectMapper objectMapper;

    public OAuth2ClientEntity from(RegisteredClient registeredClient) {
        return OAuth2ClientEntity.builder()
                .id(registeredClient.getId())
                .clientId(registeredClient.getClientId())
                .clientIdIssuedAt(
                        registeredClient.getClientIdIssuedAt() != null
                                ? registeredClient.getClientIdIssuedAt()
                                : Instant.now()
                )
                .clientSecret(registeredClient.getClientSecret())
                .clientSecretExpiresAt(registeredClient.getClientSecretExpiresAt())
                .clientName(registeredClient.getClientName())
                .clientAuthenticationMethods(
                        registeredClient.getClientAuthenticationMethods().stream()
                                .map(ClientAuthenticationMethod::getValue)
                                .collect(Collectors.toList())
                )
                .authorizationGrantTypes(
                        registeredClient.getAuthorizationGrantTypes().stream()
                                .map(AuthorizationGrantType::getValue)
                                .collect(Collectors.toList())
                )
                .redirectUris(new ArrayList<>(registeredClient.getRedirectUris()))
                .scopes(new ArrayList<>(registeredClient.getScopes()))
                .clientSettings(registeredClient.getClientSettings().getSettings())
                .tokenSettings(registeredClient.getTokenSettings().getSettings())
                .build();
    }

    public RegisteredClient toRegisteredClient(OAuth2ClientEntity oAuth2ClientEntity) {
        return RegisteredClient.withId(oAuth2ClientEntity.getId())
                .clientId(oAuth2ClientEntity.getClientId())
                .clientIdIssuedAt(oAuth2ClientEntity.getClientIdIssuedAt())
                .clientSecret(oAuth2ClientEntity.getClientSecret())
                .clientSecretExpiresAt(oAuth2ClientEntity.getClientSecretExpiresAt())
                .clientName(oAuth2ClientEntity.getClientName())
                .clientAuthenticationMethods(methods ->
                        Optional.ofNullable(oAuth2ClientEntity.getClientAuthenticationMethods()).orElse(Collections.emptyList())
                                .forEach(method -> methods.add(new ClientAuthenticationMethod(method)))
                )
                .authorizationGrantTypes(grantTypes ->
                        Optional.ofNullable(oAuth2ClientEntity.getAuthorizationGrantTypes()).orElse(Collections.emptyList())
                                .forEach(grantType -> grantTypes.add(new AuthorizationGrantType(grantType)))
                )
                .redirectUris(uris ->
                        uris.addAll(new HashSet<>(Optional.ofNullable(oAuth2ClientEntity.getRedirectUris()).orElse(Collections.emptyList()))) // List -> Set
                )
                .scopes(scopes ->
                        scopes.addAll(new HashSet<>(Optional.ofNullable(oAuth2ClientEntity.getScopes()).orElse(Collections.emptyList()))) // List -> Set
                )
                .clientSettings(ClientSettings.withSettings(
                        Optional.ofNullable(oAuth2ClientEntity.getClientSettings()).orElse(Collections.emptyMap())
                ).build())
                .tokenSettings(TokenSettings.withSettings(
                        Optional.ofNullable(oAuth2ClientEntity.getTokenSettings()).orElse(Collections.emptyMap())
                ).build())
                .build();
    }
}
