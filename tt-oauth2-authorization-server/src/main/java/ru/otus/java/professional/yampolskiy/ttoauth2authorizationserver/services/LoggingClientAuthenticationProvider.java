package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.authorization.authentication.ClientSecretAuthenticationProvider;

@Slf4j
public class LoggingClientAuthenticationProvider implements AuthenticationProvider {

    private final ClientSecretAuthenticationProvider delegate;

    public LoggingClientAuthenticationProvider(ClientSecretAuthenticationProvider delegate) {
        this.delegate = delegate;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("🔐 Client auth attempt: {}", authentication.getName());

        try {
            Authentication result = delegate.authenticate(authentication);
            log.info("✅ Client auth success: {}", authentication.getName());
            return result;
        } catch (AuthenticationException ex) {
            log.warn("❌ Client auth failed: {}", authentication.getName());
            throw ex;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return delegate.supports(authentication);
    }
}
