package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.auth.core.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.auth.core.entity.Oauth2Authorization;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.client.service.SecurityRegisteredClientRepository;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.auth.util.AuthorizationRequestSerializer;

import java.io.IOException;
import java.security.Principal;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthorizationMapper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule()) // ✅ регистрируем поддержку Instant, LocalDateTime и т.п.
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // опционально: ISO-8601 вместо epoch
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationMapper.class);
    private final SecurityRegisteredClientRepository registeredClientRepository;

    private static final String ATTR_AUTHZ_REQ_SERIALIZED = "authorization_request_serialized";
    private static final String ATTR_PRINCIPAL_NAME = "principal_name";

    public Oauth2Authorization from(OAuth2Authorization authorization) {
        Oauth2Authorization entity = Oauth2Authorization.builder()
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
            String serialized = AuthorizationRequestSerializer.serialize(authzRequest);
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

            try {
                Map<String, Object> metadata = new HashMap<>(access.getMetadata());
                convertMetadataDates(metadata);  // ✅ Конвертируем даты
                entity.setAccessTokenMetadata(OBJECT_MAPPER.writeValueAsString(metadata));
            } catch (JsonProcessingException e) {
                throw new IllegalStateException("Ошибка сериализации access_token_metadata", e);
            }
        }

        // 🔐 Refresh Token
        var refresh = authorization.getToken(OAuth2RefreshToken.class);
        if (refresh != null) {
            var token = refresh.getToken();
            entity.setRefreshTokenValue(token.getTokenValue());
            entity.setRefreshTokenIssuedAt(token.getIssuedAt());
            entity.setRefreshTokenExpiresAt(token.getExpiresAt());

            try {
                Map<String, Object> metadata = new HashMap<>(refresh.getMetadata());
                convertMetadataDates(metadata);  // ✅ Конвертируем даты
                entity.setRefreshTokenMetadata(OBJECT_MAPPER.writeValueAsString(metadata));
            } catch (JsonProcessingException e) {
                throw new IllegalStateException("Ошибка сериализации refresh_token_metadata", e);
            }
        } else {
            logger.warn("❌ [DEBUG] Refresh Token НЕ создан!");
        }

        // 🆔 ID Token

        var idToken = authorization.getToken(OidcIdToken.class);
        if (idToken != null) {
            var token = idToken.getToken();
            entity.setIdTokenValue(token.getTokenValue());
            entity.setIdTokenIssuedAt(token.getIssuedAt());
            entity.setIdTokenExpiresAt(token.getExpiresAt());
            try {
                Map<String, Object> claims = new HashMap<>(token.getClaims());
                convertMetadataDates(claims);  // ✅ Конвертируем даты
                entity.setIdTokenMetadata(OBJECT_MAPPER.writeValueAsString(claims));
            } catch (JsonProcessingException e) {
                throw new IllegalStateException("Ошибка сериализации ID Token claims", e);
            }
        }

        return entity;
    }

    public OAuth2Authorization toAuthorization(Oauth2Authorization entity) {
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
                OAuth2AuthorizationRequest deserialized = AuthorizationRequestSerializer.deserialize(serialized);
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
            try {
                Map<String, Object> metadata = OBJECT_MAPPER.readValue(entity.getAccessTokenMetadata(), new TypeReference<>() {});
                convertMetadataDates(metadata);  // ✅ Конвертируем даты перед использованием
                logger.info("🔄 Загружены метаданные Access Token: {}", metadata);

                builder.token(new OAuth2AccessToken(
                                OAuth2AccessToken.TokenType.BEARER,
                                entity.getAccessTokenValue(),
                                entity.getAccessTokenIssuedAt(),
                                entity.getAccessTokenExpiresAt(),
                                Set.of(entity.getAccessTokenScopes().split(","))),
                        claims -> claims.putAll(metadata) // ✅ Передаем метаданные
                );
            } catch (IOException e) {
                throw new IllegalStateException("Ошибка десериализации access_token_metadata", e);
            }
        }


        // 🔐 Refresh Token
        if (entity.getRefreshTokenValue() != null) {
            try {
                Map<String, Object> metadata = OBJECT_MAPPER.readValue(entity.getRefreshTokenMetadata(), new TypeReference<>() {});
                convertMetadataDates(metadata);  // ✅ Конвертируем даты перед использованием
                logger.info("🔄 Загружены метаданные Refresh Token: {}", metadata);

                builder.token(new OAuth2RefreshToken(
                                entity.getRefreshTokenValue(),
                                entity.getRefreshTokenIssuedAt(),
                                entity.getRefreshTokenExpiresAt()),
                        claims -> claims.putAll(metadata) // ✅ Передаем метаданные
                );
            } catch (IOException e) {
                throw new IllegalStateException("Ошибка десериализации refresh_token_metadata", e);
            }
        }


        // 🆔 ID Token
        if (entity.getIdTokenValue() != null) {
            Map<String, Object> claims = new HashMap<>();
            try {
                claims = OBJECT_MAPPER.readValue(entity.getIdTokenMetadata(), new TypeReference<>() {});
                convertMetadataDates(claims);  // ✅ Конвертируем даты перед использованием
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

    private void convertMetadataDates(Map<String, Object> metadata) {
        if (metadata.containsKey("metadata.token.claims")) {
            Object claimsObj = metadata.get("metadata.token.claims");
            if (claimsObj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> claims = (Map<String, Object>) claimsObj;
                convertInstantField(claims, "iat");
                convertInstantField(claims, "exp");
                convertInstantField(claims, "nbf");
            }
        }
    }

    private void convertInstantField(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof String) {
            try {
                map.put(key, Instant.parse((String) value));  // Теперь компилятор не ругается
            } catch (DateTimeParseException e) {
                logger.error("Ошибка преобразования даты {}: {}", key, value, e);
            }
        }
    }

}

