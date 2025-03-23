package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.configurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationProvider;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomOidcUserInfoMapper {
    private static final Logger logger = LoggerFactory.getLogger(CustomOidcUserInfoMapper.class);
    public void configureUserInfoMapper(OidcUserInfoAuthenticationProvider provider) {
        logger.info("Configuring user info mapper");
        provider.setUserInfoMapper(this::mapUserInfo);
    }

    private OidcUserInfo mapUserInfo(OidcUserInfoAuthenticationContext context) {
        String principalName = context.getAuthorization().getPrincipalName();
        logger.info("🧩 [mapUserInfo] User: {}", principalName);

        Map<String, Object> claims = new HashMap<>();

        logger.info("📦 Токены в authorization: accessToken={}, idToken={}",
                context.getAuthorization().getAccessToken() != null,
                context.getAuthorization().getToken(OidcIdToken.class) != null);

        var idToken = context.getAuthorization().getToken(OidcIdToken.class);
        if (idToken != null && idToken.getToken() != null) {
            claims.putAll(idToken.getToken().getClaims());
            logger.info("🧩 [mapUserInfo] Получены claims из ID токена: {}", idToken.getToken().getClaims());
        } else {
            logger.warn("⚠️ [mapUserInfo] OidcIdToken отсутствует в авторизации!");
        }

        claims.putIfAbsent("sub", principalName);
        claims.put("email", "test@example.com");
        claims.put("given_name", "John");
        claims.put("family_name", "Doe");
        claims.put("name", "John Doe");

        logger.info("🧩 [mapUserInfo] Final claims: {}", claims);
        return new OidcUserInfo(claims);
    }
}
