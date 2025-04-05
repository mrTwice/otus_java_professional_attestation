package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.auth.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.integrations.users.dto.UserPrincipalDTO;

import java.util.HashSet;
import java.util.Set;

@Configuration
@Slf4j
public class AccessTokenCustomizerConfig {

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> accessTokenCustomizer() {
        return context -> {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                context.getClaims().claims(claims -> {
                    claims.put("client_id", context.getRegisteredClient().getClientId());
                    claims.put("user_name", context.getPrincipal().getName());


                    if (context.getPrincipal() instanceof UsernamePasswordAuthenticationToken auth &&
                            auth.getPrincipal() instanceof UserPrincipalDTO user) {

                        // ✅ ВАЖНО: scope = ВСЕ разрешённые пермишены пользователя
                        Set<String> fullScope = user.getPermissions();
                        claims.put("scope", String.join(" ", fullScope));

                        claims.put("permissions", fullScope);
                        claims.put("roles", user.getRoles());
                        claims.put("sub", user.getOidcSubject().toString());
                    }
                });
            }
        };
    }

//    @Bean
//    public OAuth2TokenCustomizer<JwtEncodingContext> accessTokenCustomizer() {
//        return context -> {
//            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
//                log.info("🎯 Customizing access token for client: {}", context.getRegisteredClient().getClientId());
//
//                context.getClaims().claims(claims -> {
//                    RegisteredClient client = context.getRegisteredClient();
//                    claims.put("client_id", client.getClientId()); // 💥 это добавит то, что нужно
//
//                    if (context.getPrincipal() instanceof UsernamePasswordAuthenticationToken auth &&
//                            auth.getPrincipal() instanceof UserPrincipalDTO user) {
//
//                        claims.put("user_name", user.getUsername());
//                        claims.put("sub", user.getOidcSubject().toString());
//                        claims.put("permissions", user.getPermissions());
//                        claims.put("roles", user.getRoles());
//                        claims.put("scope", String.join(" ", user.getPermissions()));
//                    }
//
//                    // 💡 Обработка client_credentials
//                    if (context.getAuthorizationGrantType().equals(AuthorizationGrantType.CLIENT_CREDENTIALS)) {
//                        claims.put("sub", client.getClientId()); // допустимо по JWT spec
//                        claims.put("scope", String.join(" ", context.getAuthorizedScopes()));
//                    }
//                });
//            }
//        };
//    }

//    @Bean
//    public OAuth2TokenCustomizer<JwtEncodingContext> accessTokenCustomizer() {
//        return context -> {
//            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
//                context.getClaims().claims(claims -> {
//                    claims.put("client_id", context.getRegisteredClient().getClientId());
//
//                    if (context.getPrincipal() instanceof UsernamePasswordAuthenticationToken auth &&
//                            auth.getPrincipal() instanceof UserPrincipalDTO user) {
//
//                        // Все разрешённые пермишены пользователя
//                        Set<String> userPermissions = user.getPermissions();
//
//                        // Добавляем полезные мета-данные
//                        claims.put("user_name", user.getUsername());
//                        claims.put("permissions", userPermissions);
//                        claims.put("roles", user.getRoles());
//                        claims.put("sub", user.getOidcSubject().toString());
//
//                        // Вычисляем scope: только те, что разрешены и клиентом, и пользователем
//                        Set<String> effectiveScopes = new HashSet<>(context.getAuthorizedScopes());
//                        effectiveScopes.retainAll(userPermissions);
//                        claims.put("scope", String.join(" ", effectiveScopes));
//                    } else {
//                        // machine-to-machine (client_credentials)
//                        Set<String> scopes = context.getAuthorizedScopes();
//                        claims.put("scope", String.join(" ", scopes));
//                    }
//                });
//            }
//        };
//    }
//
}
