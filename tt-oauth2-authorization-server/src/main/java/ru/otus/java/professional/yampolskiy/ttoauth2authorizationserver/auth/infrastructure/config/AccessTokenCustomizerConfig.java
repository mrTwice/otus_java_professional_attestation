package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.auth.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.integrations.users.dto.UserPrincipalDTO;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.oidc.service.UserAuthCache;

import java.util.ArrayList;

@Configuration
@Slf4j
public class AccessTokenCustomizerConfig {

//    @Bean
//    public OAuth2TokenCustomizer<JwtEncodingContext> accessTokenCustomizer() {
//        log.info("🛠 accessTokenCustomizer initializing ");
//        return context -> {
//            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
//                log.info("🛠 ACCESS_TOKEN in CONTEXT ");
//                context.getClaims().claims(claims -> {
//                    claims.put("client_id", context.getRegisteredClient().getClientId());
//                    claims.put("user_name", context.getPrincipal().getName());
//
//                    if (context.getPrincipal() instanceof UsernamePasswordAuthenticationToken auth &&
//                            auth.getPrincipal() instanceof UserPrincipalDTO user) {
//                        log.info("🛠 context.getPrincipal is UsernamePasswordAuthenticationToken &  auth.getPrincipal() is UserPrincipalDTO ");
//                        Set<String> fullScope = user.getPermissions();
//
//                        claims.put("scope", String.join(" ", fullScope)); // для OIDC совместимости
//                        claims.put("permissions", new ArrayList<>(fullScope)); // ✅ обязательно как List
//                        claims.put("roles", new ArrayList<>(user.getRoles())); // ✅ тоже как List
//                        claims.put("sub", user.getOidcSubject().toString());
//
//                        log.info("🛠 Access token claims: {}", claims);
//                    }
//                });
//            }
//        };
//    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> accessTokenCustomizer(UserAuthCache userAuthCache) {
        log.info("🛠 AccessTokenCustomizer initializing");

        return context -> {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                String username = context.getPrincipal().getName();
                log.info("🛠 ACCESS_TOKEN for: {}", username);

                UserPrincipalDTO user = userAuthCache.get(username);
                if (user != null) {
                    log.info("✅ Found user in UserAuthCache: {}", user.getUsername());

                    context.getClaims().claims(claims -> {
                        claims.put("permissions", new ArrayList<>(user.getPermissions()));
                        claims.put("roles", new ArrayList<>(user.getRoles()));
                        claims.put("scope", String.join(" ", user.getPermissions()));
                        claims.put("sub", user.getOidcSubject().toString());
                        claims.put("user_name", user.getUsername());
                        claims.put("client_id", context.getRegisteredClient().getClientId());
                    });

                    userAuthCache.remove(username);

                } else {
                    log.warn("⚠️ UserPrincipalDTO not found in UserAuthCache for: {}", username);
                }
            }
        };
    }
}
