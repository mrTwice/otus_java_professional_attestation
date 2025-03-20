package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.entities.OAuth2ClientEntity;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class OAuth2ClientMapper {

    private final ObjectMapper mapper;

    public OAuth2ClientEntity from(RegisteredClient registeredClient) {
        return OAuth2ClientEntity.builder()
                .id(registeredClient.getId())
                .clientId(registeredClient.getClientId())
                .clientIdIssuedAt(registeredClient.getClientIdIssuedAt())
                .clientSecret(registeredClient.getClientSecret())
                .clientSecretExpiresAt(registeredClient.getClientSecretExpiresAt())
                .clientName(registeredClient.getClientName())
                .clientAuthenticationMethods(
                        registeredClient.getClientAuthenticationMethods().stream()
                                .map(ClientAuthenticationMethod::getValue)
                                .collect(Collectors.joining(","))
                )
                .authorizationGrantTypes(
                        registeredClient.getAuthorizationGrantTypes().stream()
                                .map(AuthorizationGrantType::getValue)
                                .collect(Collectors.joining(","))
                )
                .redirectUris(String.join(",", registeredClient.getRedirectUris()))
                .scopes(String.join(",", registeredClient.getScopes()))
                .clientSettings(serializeSettings(registeredClient.getClientSettings()))
                .tokenSettings(serializeSettings(registeredClient.getTokenSettings()))
                .build();
    }

    private String serializeSettings(Object settings) {
        try {
            return mapper.writeValueAsString(settings);
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize settings", e);
        }
    }


    public RegisteredClient toRegisteredClient(OAuth2ClientEntity oAuth2ClientEntity) {

        return RegisteredClient.withId(oAuth2ClientEntity.getId())
                .clientId(oAuth2ClientEntity.getClientId())
                .clientIdIssuedAt(oAuth2ClientEntity.getClientIdIssuedAt())
                .clientSecret(oAuth2ClientEntity.getClientSecret())
                .clientSecretExpiresAt(oAuth2ClientEntity.getClientSecretExpiresAt())
                .clientName(oAuth2ClientEntity.getClientName())
                .clientAuthenticationMethods(methods ->
                        Stream.of(oAuth2ClientEntity.getClientAuthenticationMethods().split(","))
                                .map(ClientAuthenticationMethod::new)
                                .forEach(methods::add)
                )
                .authorizationGrantTypes(grantTypes ->
                        Stream.of(oAuth2ClientEntity.getAuthorizationGrantTypes().split(","))
                                .map(AuthorizationGrantType::new)
                                .forEach(grantTypes::add)
                )
                .redirectUris(uris -> uris.addAll(Set.of(oAuth2ClientEntity.getRedirectUris().split(","))))
                .scopes(scopes -> scopes.addAll(Set.of(oAuth2ClientEntity.getScopes().split(","))))
                .clientSettings(ClientSettings.withSettings(deserializeSettings(oAuth2ClientEntity.getClientSettings())).build())
                .tokenSettings(TokenSettings.withSettings(deserializeSettings(oAuth2ClientEntity.getTokenSettings())).build())
                .build();
    }

    private Map<String, Object> deserializeSettings(String settingsJson) {
        try {
            return mapper.readValue(settingsJson, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize settings", e);
        }
    }
}
