package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Service;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.entities.OAuth2AuthorizationEntity;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.repositories.JpaRegisteredClientRepository;

import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class OAuth2AuthorizationMapper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule()) // ✅ регистрируем поддержку Instant, LocalDateTime и т.п.
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // опционально: ISO-8601 вместо epoch
    private static final Logger logger = Logger.getLogger(OAuth2AuthorizationMapper.class.getName());
    private final JpaRegisteredClientRepository registeredClientRepository;

    private static final String ATTR_AUTHZ_REQ_SERIALIZED = "authorization_request_serialized";
    private static final String ATTR_PRINCIPAL_NAME = "principal_name";

    public OAuth2AuthorizationEntity from(OAuth2Authorization authorization) {
        OAuth2AuthorizationEntity entity = OAuth2AuthorizationEntity.builder()
                .id(authorization.getId())
                .registeredClientId(authorization.getRegisteredClientId())
                .principalName(authorization.getPrincipalName())
                .authorizationGrantType(authorization.getAuthorizationGrantType().getValue())
                .authorizedScopes(String.join(",", authorization.getAuthorizedScopes()))
                .state(authorization.getAttribute("state"))
                .build();

        Map<String, Object> attributes = new HashMap<>(authorization.getAttributes());

        // ❌ Удаляем объекты, которые не сериализуются
        attributes.entrySet().removeIf(entry ->
                entry.getValue() instanceof Authentication ||
                        entry.getValue() instanceof Principal
        );
        attributes.remove(Principal.class.getName());
        attributes.remove(Authentication.class.getName());

        // 🧠 Сохраняем OAuth2AuthorizationRequest
        OAuth2AuthorizationRequest authzRequest = authorization.getAttribute(OAuth2AuthorizationRequest.class.getName());
        if (authzRequest != null) {
            String serialized = OAuth2AuthorizationRequestUtils.serialize(authzRequest);
            attributes.put(ATTR_AUTHZ_REQ_SERIALIZED, serialized);
        }

        // 💾 Добавляем имя пользователя
        attributes.put(ATTR_PRINCIPAL_NAME, authorization.getPrincipalName());

        try {

            entity.setAttributes(OBJECT_MAPPER.writeValueAsString(attributes));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Ошибка сериализации attributes", e);
        }

        // 🔐 Authorization Code
        var code = authorization.getToken(OAuth2AuthorizationCode.class);
        if (code != null) {
            entity.setAuthorizationCodeValue(code.getToken().getTokenValue());
            entity.setAuthorizationCodeIssuedAt(code.getToken().getIssuedAt());
            entity.setAuthorizationCodeExpiresAt(code.getToken().getExpiresAt());
        }

        // 🔐 Access Token
        var access = authorization.getToken(OAuth2AccessToken.class);
        if (access != null) {
            var token = access.getToken();
            entity.setAccessTokenValue(token.getTokenValue());
            entity.setAccessTokenIssuedAt(token.getIssuedAt());
            entity.setAccessTokenExpiresAt(token.getExpiresAt());
            entity.setAccessTokenType(token.getTokenType().getValue());
            entity.setAccessTokenScopes(String.join(",", token.getScopes()));
        }

        // 🔐 Refresh Token
        var refresh = authorization.getToken(OAuth2RefreshToken.class);
        if (refresh != null) {
            var token = refresh.getToken();
            entity.setRefreshTokenValue(token.getTokenValue());
            entity.setRefreshTokenIssuedAt(token.getIssuedAt());
            entity.setRefreshTokenExpiresAt(token.getExpiresAt());
        }

        // 🆔 ID Token

        var idToken = authorization.getToken(OidcIdToken.class);
        if (idToken != null) {
            var token = idToken.getToken();
            entity.setIdTokenValue(token.getTokenValue());
            entity.setIdTokenIssuedAt(token.getIssuedAt());
            entity.setIdTokenExpiresAt(token.getExpiresAt());
            try {
                entity.setIdTokenMetadata(OBJECT_MAPPER.writeValueAsString(token.getClaims()));
            } catch (JsonProcessingException e) {
                throw new IllegalStateException("Ошибка сериализации ID Token claims", e);
            }
        }

        return entity;
    }

    public OAuth2Authorization toAuthorization(OAuth2AuthorizationEntity entity) {
        RegisteredClient client = registeredClientRepository.findById(entity.getRegisteredClientId());
        if (client == null) {
            throw new IllegalArgumentException("Client not found: " + entity.getRegisteredClientId());
        }

        OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(client)
                .id(entity.getId())
                .principalName(entity.getPrincipalName())
                .authorizationGrantType(new AuthorizationGrantType(entity.getAuthorizationGrantType()))
                .authorizedScopes(Set.of(entity.getAuthorizedScopes().split(",")));

        if (entity.getState() != null) {
            builder.attribute("state", entity.getState());
        }

        try {
            Map<String, Object> attributes = OBJECT_MAPPER.readValue(entity.getAttributes(), new TypeReference<>() {});

            // 🔁 Восстановление OAuth2AuthorizationRequest
            if (attributes.containsKey(ATTR_AUTHZ_REQ_SERIALIZED)) {
                String serialized = (String) attributes.get(ATTR_AUTHZ_REQ_SERIALIZED);
                OAuth2AuthorizationRequest deserialized = OAuth2AuthorizationRequestUtils.deserialize(serialized);
                attributes.put(OAuth2AuthorizationRequest.class.getName(), deserialized);
            }

            // 🔁 Восстановление Authentication
            if (attributes.containsKey(ATTR_PRINCIPAL_NAME)) {
                String username = (String) attributes.get(ATTR_PRINCIPAL_NAME);
                // Здесь можно подставить настоящие роли/authorities, если нужно
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, List.of());
                attributes.put(Principal.class.getName(), authentication);
            }

            builder.attributes(attrs -> attrs.putAll(attributes));
        } catch (IOException e) {
            throw new IllegalStateException("Ошибка десериализации attributes", e);
        }

        // 🔐 Authorization Code
        if (entity.getAuthorizationCodeValue() != null) {
            builder.token(new OAuth2AuthorizationCode(
                    entity.getAuthorizationCodeValue(),
                    entity.getAuthorizationCodeIssuedAt(),
                    entity.getAuthorizationCodeExpiresAt()
            ));
        }

        // 🔐 Access Token
        if (entity.getAccessTokenValue() != null) {
            builder.token(new OAuth2AccessToken(
                    OAuth2AccessToken.TokenType.BEARER,
                    entity.getAccessTokenValue(),
                    entity.getAccessTokenIssuedAt(),
                    entity.getAccessTokenExpiresAt(),
                    Set.of(entity.getAccessTokenScopes().split(","))
            ));
        }

        // 🔐 Refresh Token
        if (entity.getRefreshTokenValue() != null) {
            builder.token(new OAuth2RefreshToken(
                    entity.getRefreshTokenValue(),
                    entity.getRefreshTokenIssuedAt(),
                    entity.getRefreshTokenExpiresAt()
            ));
        }

        // 🆔 ID Token
        if (entity.getIdTokenValue() != null) {
            Map<String, Object> claims = new HashMap<>();
            try {
                claims = OBJECT_MAPPER.readValue(entity.getIdTokenMetadata(), new TypeReference<>() {});
            } catch (IOException e) {
                throw new IllegalStateException("Ошибка десериализации ID Token claims", e);
            }

            builder.token(new OidcIdToken(
                    entity.getIdTokenValue(),
                    entity.getIdTokenIssuedAt(),
                    entity.getIdTokenExpiresAt(),
                    claims
            ));
        }

        return builder.build();
    }
}

