package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.configurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationToken;


import java.util.HashMap;
import java.util.Map;

//@Component
public class CustomOidcUserInfoAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomOidcUserInfoAuthenticationProvider.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LOGGER.info("🎯 Вызван CustomOidcUserInfoAuthenticationProvider");
        OidcUserInfoAuthenticationToken token = (OidcUserInfoAuthenticationToken) authentication;
        Authentication principal = (Authentication) token.getPrincipal();
        LOGGER.info("🔍 principal = {}", principal);
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", principal.getName()); // обязательный
        claims.put("email", principal.getName() + "@example.com");
        claims.put("name", "User " + principal.getName());
        claims.put("preferred_username", principal.getName());

        OidcUserInfo userInfo = new OidcUserInfo(claims);

        return new OidcUserInfoAuthenticationToken(principal, userInfo);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OidcUserInfoAuthenticationToken.class.isAssignableFrom(authentication);
    }
}