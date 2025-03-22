package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {

    public CustomDaoAuthenticationProvider(UserDetailsService userDetailsService,
                                           PasswordEncoder passwordEncoder) {
        setUserDetailsService(userDetailsService);
        setPasswordEncoder(passwordEncoder);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("👤 User authentication attempt: {}", authentication.getName());
        Authentication result = super.authenticate(authentication);
        log.info("✅ User authenticated: {}", result.isAuthenticated());
        return result;
    }
}
