package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.auth.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

@Configuration
public class AccessTokenCustomizerConfig {

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> accessTokenCustomizer() {
        return (context) -> {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                context.getClaims().claims(claims -> {
                    claims.put("scope", String.join(" ", context.getAuthorizedScopes()));
                    claims.put("client_id", context.getRegisteredClient().getClientId());
                    claims.put("user_name", context.getPrincipal().getName());
                });

            }
        };
    }

}