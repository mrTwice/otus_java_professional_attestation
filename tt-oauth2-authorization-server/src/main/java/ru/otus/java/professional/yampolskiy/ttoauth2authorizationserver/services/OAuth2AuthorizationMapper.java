package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Service;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.entities.OAuth2AuthorizationEntity;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.repositories.JpaRegisteredClientRepository;

import java.util.Set;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class OAuth2AuthorizationMapper {

    private static final Logger logger = Logger.getLogger(OAuth2AuthorizationMapper.class.getName());
    private final JpaRegisteredClientRepository registeredClientRepository;


    public OAuth2AuthorizationEntity from(OAuth2Authorization authorization) {
        OAuth2AuthorizationEntity entity = OAuth2AuthorizationEntity.builder()
                .id(authorization.getId())
                .registeredClientId(authorization.getRegisteredClientId())
                .principalName(authorization.getPrincipalName())
                .authorizationGrantType(authorization.getAuthorizationGrantType().getValue())
                .authorizedScopes(String.join(",", authorization.getAuthorizedScopes()))
                .state(authorization.getAttribute("state"))
                .build();

        // Authorization Code
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCodeToken = authorization.getToken(OAuth2AuthorizationCode.class);
        if (authorizationCodeToken != null) {
            OAuth2AuthorizationCode authorizationCode = authorizationCodeToken.getToken();
            entity.setAuthorizationCodeValue(String.valueOf(authorizationCode.getTokenValue()));
            entity.setAuthorizationCodeIssuedAt(authorizationCode.getIssuedAt());
            entity.setAuthorizationCodeExpiresAt(authorizationCode.getExpiresAt());
        }

        // Access Token
        OAuth2Authorization.Token<OAuth2AccessToken> accessTokenToken = authorization.getToken(OAuth2AccessToken.class);
        if (accessTokenToken != null) {
            OAuth2AccessToken accessToken = accessTokenToken.getToken();
            entity.setAccessTokenValue(String.valueOf(accessToken.getTokenValue()));
            entity.setAccessTokenIssuedAt(accessToken.getIssuedAt());
            entity.setAccessTokenExpiresAt(accessToken.getExpiresAt());
            entity.setAccessTokenType(accessToken.getTokenType().getValue());
            entity.setAccessTokenScopes(String.join(",", accessToken.getScopes()));
            logger.info("Info token: " + accessToken.getTokenValue() + " (" + accessToken.getTokenValue().getClass().getSimpleName() + ")");

        }

        // Refresh Token
        OAuth2Authorization.Token<OAuth2RefreshToken> refreshTokenToken = authorization.getToken(OAuth2RefreshToken.class);
        if (refreshTokenToken != null) {
            OAuth2RefreshToken refreshToken = refreshTokenToken.getToken();
            entity.setRefreshTokenValue(String.valueOf(refreshToken.getTokenValue()));
            entity.setRefreshTokenIssuedAt(refreshToken.getIssuedAt());
            entity.setRefreshTokenExpiresAt(refreshToken.getExpiresAt());
        }

        return entity;
    }


    public OAuth2Authorization toAuthorization(OAuth2AuthorizationEntity oAuth2AuthorizationEntity) {
        // Получаем RegisteredClient по registeredClientId
        RegisteredClient registeredClient = registeredClientRepository.findById(oAuth2AuthorizationEntity.getRegisteredClientId());
        if (registeredClient == null) {
            throw new IllegalArgumentException("RegisteredClient not found for clientId: " + oAuth2AuthorizationEntity.getRegisteredClientId());
        }

        // Создаем Builder с указанием RegisteredClient
        OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .id(oAuth2AuthorizationEntity.getId())
                .principalName(oAuth2AuthorizationEntity.getPrincipalName())
                .authorizationGrantType(new AuthorizationGrantType(oAuth2AuthorizationEntity.getAuthorizationGrantType()))
                .authorizedScopes(Set.of(oAuth2AuthorizationEntity.getAuthorizedScopes().split(",")));
        if (oAuth2AuthorizationEntity.getState() != null) {
            builder.attribute("state", oAuth2AuthorizationEntity.getState());
        }

        // Authorization Code
        if (oAuth2AuthorizationEntity.getAuthorizationCodeValue() != null) {
            builder.token(new OAuth2AuthorizationCode(
                    oAuth2AuthorizationEntity.getAuthorizationCodeValue(),
                    oAuth2AuthorizationEntity.getAuthorizationCodeIssuedAt(),
                    oAuth2AuthorizationEntity.getAuthorizationCodeExpiresAt()
            ));
        }

        // Access Token
        if (oAuth2AuthorizationEntity.getAccessTokenValue() != null) {
            builder.token(new OAuth2AccessToken(
                    OAuth2AccessToken.TokenType.BEARER,
                    oAuth2AuthorizationEntity.getAccessTokenValue(),
                    oAuth2AuthorizationEntity.getAccessTokenIssuedAt(),
                    oAuth2AuthorizationEntity.getAccessTokenExpiresAt(),
                    Set.of(oAuth2AuthorizationEntity.getAccessTokenScopes().split(","))
            ));
        }

        // Refresh Token
        if (oAuth2AuthorizationEntity.getRefreshTokenValue() != null) {
            builder.token(new OAuth2RefreshToken(
                    oAuth2AuthorizationEntity.getRefreshTokenValue(),
                    oAuth2AuthorizationEntity.getRefreshTokenIssuedAt(),
                    oAuth2AuthorizationEntity.getRefreshTokenExpiresAt()
            ));
        }

        return builder.build();
    }
}