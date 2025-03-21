package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.repositories.OAuth2AuthorizationRepository;

@Service
@RequiredArgsConstructor
public class JpaOAuth2AuthorizationService implements OAuth2AuthorizationService{

    private static final Logger logger = LoggerFactory.getLogger(JpaOAuth2AuthorizationService.class);
    private final OAuth2AuthorizationRepository authorizationRepository;
    private final OAuth2AuthorizationMapper authorizationMapper;


    @Override
    public void save(OAuth2Authorization authorization) {
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
        if (tokenType == null) {
            // Если тип токена не указан, ищем по любому типу токена
            return authorizationRepository.findByTokenValue(token)
                    .map(authorizationMapper::toAuthorization)
                    .orElse(null);
        }

        // Ищем авторизацию по конкретному типу токена
        return switch (tokenType.getValue()) {
            case "access_token" -> authorizationRepository.findByAccessTokenValue(token)
                    .map(authorizationMapper::toAuthorization)
                    .orElse(null);
            case "refresh_token" -> authorizationRepository.findByRefreshTokenValue(token)
                    .map(authorizationMapper::toAuthorization)
                    .orElse(null);
            case "authorization_code" -> authorizationRepository.findByAuthorizationCodeValue(token)
                    .map(authorizationMapper::toAuthorization)
                    .orElse(null);
            default -> throw new IllegalArgumentException("Unsupported token type: " + tokenType.getValue());
        };
    }
}

