package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.configurations;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeRequestAuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;

@Component
public class LoggingOAuth2AuthorizationCodeRequestConverter
        implements AuthenticationConverter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingOAuth2AuthorizationCodeRequestConverter.class);
    private final OAuth2AuthorizationCodeRequestAuthenticationConverter delegate =
            new OAuth2AuthorizationCodeRequestAuthenticationConverter();

    @Override
    public Authentication convert(HttpServletRequest request) {
        Authentication authentication = delegate.convert(request);

        if (authentication instanceof OAuth2AuthorizationCodeRequestAuthenticationToken token) {
            String state = token.getState();
            logger.info("📥 State извлечен в Authorization Code Flow: {}", state != null ? state : "❌ (нет в запросе)");
        }

        return authentication;
    }
}