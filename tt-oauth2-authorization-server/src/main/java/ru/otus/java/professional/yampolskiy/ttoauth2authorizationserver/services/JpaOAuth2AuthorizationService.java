package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.repositories.OAuth2AuthorizationRepository;

import java.util.List;
import java.util.stream.Stream;

@Service
public class JpaOAuth2AuthorizationService implements OAuth2AuthorizationService{

    private static final Logger logger = LoggerFactory.getLogger(JpaOAuth2AuthorizationService.class);
    private final OAuth2AuthorizationRepository authorizationRepository;
    private final OAuth2AuthorizationMapper authorizationMapper;

    public JpaOAuth2AuthorizationService(OAuth2AuthorizationRepository authorizationRepository, OAuth2AuthorizationMapper authorizationMapper) {
        this.authorizationRepository = authorizationRepository;
        this.authorizationMapper = authorizationMapper;
        logger.info("🔥 JpaOAuth2AuthorizationService initialized");
    }

    @Override
    public void save(OAuth2Authorization authorization) {
        List<String> tokens = Stream.of(
                OAuth2AccessToken.class,
                OAuth2RefreshToken.class,
                OidcIdToken.class
        ).map(type -> {
            var token = authorization.getToken(type);
            return token != null ? type.getSimpleName() + ": ✅" : type.getSimpleName() + ": ❌";
        }).toList();

        logger.info("🧪 Сохраняем токены: {}", tokens);

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

