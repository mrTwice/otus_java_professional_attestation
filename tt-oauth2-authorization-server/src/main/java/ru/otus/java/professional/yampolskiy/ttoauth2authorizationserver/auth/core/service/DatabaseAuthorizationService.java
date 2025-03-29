package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.auth.core.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.auth.core.mapper.AuthorizationMapper;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.auth.core.repository.AuthorizationRepository;

import java.util.Objects;
import java.util.Optional;

@Service
public class DatabaseAuthorizationService implements OAuth2AuthorizationService{

    private static final Logger logger = LoggerFactory.getLogger(DatabaseAuthorizationService.class);
    private final AuthorizationRepository authorizationRepository;
    private final AuthorizationMapper authorizationMapper;

    public DatabaseAuthorizationService(AuthorizationRepository authorizationRepository, AuthorizationMapper authorizationMapper) {
        this.authorizationRepository = authorizationRepository;
        this.authorizationMapper = authorizationMapper;
        logger.info("🔥 JpaOAuth2AuthorizationService initialized");
    }

    @Override
    public void save(OAuth2Authorization authorization) {
        OAuth2AccessToken accessToken = Optional.ofNullable(authorization.getToken(OAuth2AccessToken.class))
                .map(OAuth2Authorization.Token::getToken)
                .orElse(null);

        OAuth2RefreshToken refreshToken = Optional.ofNullable(authorization.getToken(OAuth2RefreshToken.class))
                .map(OAuth2Authorization.Token::getToken)
                .orElse(null);

        OidcIdToken idToken = Optional.ofNullable(authorization.getToken(OidcIdToken.class))
                .map(OAuth2Authorization.Token::getToken)
                .orElse(null);

        String state = authorization.getAttribute(OAuth2ParameterNames.STATE);
        logger.info("🧪 Сохраняем токены: AccessToken: {}, RefreshToken: {}, IdToken: {}, State: {}",
                accessToken != null ? "✅" : "❌",
                refreshToken != null ? "✅" : "❌",
                idToken != null ? "✅" : "❌",
                state != null ? state : "❌ (не передан)");

        OAuth2Authorization.Token<OAuth2AccessToken> accessToken1 = authorization.getToken(OAuth2AccessToken.class);
        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken2 = authorization.getToken(OAuth2RefreshToken.class);

        logger.info("🔍 Access Token Metadata: {}",
                (accessToken != null && Objects.requireNonNull(accessToken1).getMetadata() != null)
                        ? accessToken1.getMetadata()
                        : "null");

        logger.info("🔍 Refresh Token Metadata: {}",
                (refreshToken != null && Objects.requireNonNull(refreshToken2).getMetadata() != null)
                        ? refreshToken2.getMetadata()
                        : "null");
        if (refreshToken == null) {
            logger.warn("❌ Refresh Token НЕ СОЗДАН! Проверь конфигурацию клиента и scope!");
        }

        authorizationRepository.save(authorizationMapper.from(authorization));
    }


    @Override
    public void remove(OAuth2Authorization authorization) {
        logger.info("Удаляем токен: {}", authorization.getId());
        authorizationRepository.deleteById(authorization.getId());
        if (!authorizationRepository.existsById(authorization.getId())) {
            logger.warn("Токен уже удалён или не найден: {}", authorization.getId());
        } else {
            logger.warn("Токен не удалён: {}", authorization.getId());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public OAuth2Authorization findById(String id) {
        return authorizationRepository.findById(id)
                .map(authorizationMapper::toAuthorization)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        logger.info("🔥 [findByToken] token = {}, tokenType = {}", token, tokenType);
        OAuth2Authorization authorization = null;

        if (tokenType == null) {
            authorization = authorizationRepository.findByTokenValue(token)
                    .map(authorizationMapper::toAuthorization)
                    .orElse(null);
        } else {
            switch (tokenType.getValue()) {
                case "access_token" -> authorization = authorizationRepository.findByAccessTokenValue(token)
                        .map(authorizationMapper::toAuthorization)
                        .orElse(null);
                case "refresh_token" -> authorization = authorizationRepository.findByRefreshTokenValue(token)
                        .map(authorizationMapper::toAuthorization)
                        .orElse(null);
                case "authorization_code", "code" -> authorization = authorizationRepository.findByAuthorizationCodeValue(token)
                        .map(authorizationMapper::toAuthorization)
                        .orElse(null);
                case "state" -> authorization = authorizationRepository.findByState(token)
                        .map(authorizationMapper::toAuthorization)
                        .orElse(null);
                default -> throw new IllegalArgumentException("Unsupported token type: " + tokenType.getValue());
            }
        }

        if (authorization == null) {
            logger.warn("❌ Токен не найден: {}, тип: {}", token, tokenType != null ? tokenType.getValue() : "null");
        } else {
            logger.info("✅ Найдена авторизация: {}", authorization.getId());
        }

        return authorization;
    }
}

