package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.services;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Service;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.entities.OAuth2AuthorizationEntity;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.repositories.JpaRegisteredClientRepository;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class OAuth2AuthorizationMapper {

    private static final Logger logger = Logger.getLogger(OAuth2AuthorizationMapper.class.getName());
    private final JpaRegisteredClientRepository registeredClientRepository;

    private static final String ATTR_AUTHZ_REQ_SERIALIZED = "authorization_request_serialized";

    public OAuth2AuthorizationEntity from(OAuth2Authorization authorization) {
        OAuth2AuthorizationEntity entity = OAuth2AuthorizationEntity.builder()
                .id(authorization.getId())
                .registeredClientId(authorization.getRegisteredClientId())
                .principalName(authorization.getPrincipalName())
                .authorizationGrantType(authorization.getAuthorizationGrantType().getValue())
                .authorizedScopes(String.join(",", authorization.getAuthorizedScopes()))
                .state(authorization.getAttribute("state"))
                .build();

        // 🧠 Сохраняем сериализованный authorizationRequest
        Map<String, Object> attributes = new HashMap<>(authorization.getAttributes());
        OAuth2AuthorizationRequest authzRequest = authorization.getAttribute(OAuth2AuthorizationRequest.class.getName());
        if (authzRequest != null) {
            String serialized = OAuth2AuthorizationRequestUtils.serialize(authzRequest);
            attributes.put("authorization_request_serialized", serialized);
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            entity.setAttributes(objectMapper.writeValueAsString(attributes));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Ошибка сериализации attributes", e);
        }

        // Токены (авторизационный, access, refresh) – как раньше:
        var code = authorization.getToken(OAuth2AuthorizationCode.class);
        if (code != null) {
            entity.setAuthorizationCodeValue(code.getToken().getTokenValue());
            entity.setAuthorizationCodeIssuedAt(code.getToken().getIssuedAt());
            entity.setAuthorizationCodeExpiresAt(code.getToken().getExpiresAt());
        }

        var access = authorization.getToken(OAuth2AccessToken.class);
        if (access != null) {
            var token = access.getToken();
            entity.setAccessTokenValue(token.getTokenValue());
            entity.setAccessTokenIssuedAt(token.getIssuedAt());
            entity.setAccessTokenExpiresAt(token.getExpiresAt());
            entity.setAccessTokenType(token.getTokenType().getValue());
            entity.setAccessTokenScopes(String.join(",", token.getScopes()));
        }

        var refresh = authorization.getToken(OAuth2RefreshToken.class);
        if (refresh != null) {
            var token = refresh.getToken();
            entity.setRefreshTokenValue(token.getTokenValue());
            entity.setRefreshTokenIssuedAt(token.getIssuedAt());
            entity.setRefreshTokenExpiresAt(token.getExpiresAt());
        }

        return entity;
    }

    public OAuth2Authorization toAuthorization(OAuth2AuthorizationEntity entity) {
        RegisteredClient client = registeredClientRepository.findById(entity.getRegisteredClientId());
        if (client == null) throw new IllegalArgumentException("Client not found: " + entity.getRegisteredClientId());

        OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(client)
                .id(entity.getId())
                .principalName(entity.getPrincipalName())
                .authorizationGrantType(new AuthorizationGrantType(entity.getAuthorizationGrantType()))
                .authorizedScopes(Set.of(entity.getAuthorizedScopes().split(",")));

        if (entity.getState() != null) {
            builder.attribute("state", entity.getState());
        }

        // 🔄 Десериализация attributes
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> attributes = objectMapper.readValue(entity.getAttributes(), new TypeReference<>() {});
            if (attributes.containsKey("authorization_request_serialized")) {
                String serialized = (String) attributes.get("authorization_request_serialized");
                OAuth2AuthorizationRequest deserialized = OAuth2AuthorizationRequestUtils.deserialize(serialized);
                attributes.put(OAuth2AuthorizationRequest.class.getName(), deserialized);
            }
            builder.attributes(attrs -> attrs.putAll(attributes));
        } catch (IOException e) {
            throw new IllegalStateException("Ошибка десериализации attributes", e);
        }

        // Токены
        if (entity.getAuthorizationCodeValue() != null) {
            builder.token(new OAuth2AuthorizationCode(
                    entity.getAuthorizationCodeValue(),
                    entity.getAuthorizationCodeIssuedAt(),
                    entity.getAuthorizationCodeExpiresAt()
            ));
        }

        if (entity.getAccessTokenValue() != null) {
            builder.token(new OAuth2AccessToken(
                    OAuth2AccessToken.TokenType.BEARER,
                    entity.getAccessTokenValue(),
                    entity.getAccessTokenIssuedAt(),
                    entity.getAccessTokenExpiresAt(),
                    Set.of(entity.getAccessTokenScopes().split(","))
            ));
        }

        if (entity.getRefreshTokenValue() != null) {
            builder.token(new OAuth2RefreshToken(
                    entity.getRefreshTokenValue(),
                    entity.getRefreshTokenIssuedAt(),
                    entity.getRefreshTokenExpiresAt()
            ));
        }

        return builder.build();
    }
}
