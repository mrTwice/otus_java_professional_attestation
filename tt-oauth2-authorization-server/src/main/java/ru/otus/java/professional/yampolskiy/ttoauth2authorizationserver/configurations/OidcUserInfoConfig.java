package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.configurations;

import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.Map;

@Component
public class OidcUserInfoConfig implements OidcUserInfoMapper {

    @Override
    public OidcUserInfo map(OidcUserInfoAuthenticationContext context) {
        // Получаем имя пользователя
        String username = context.getAuthorization().getPrincipalName();

        // Здесь ты можешь вытащить информацию из своего UserService/БД
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", username);
        claims.put("email", username + "@example.com");
        claims.put("name", "User " + username);
        claims.put("given_name", "Test");
        claims.put("family_name", "User");

        return new OidcUserInfo(claims);
    }
}