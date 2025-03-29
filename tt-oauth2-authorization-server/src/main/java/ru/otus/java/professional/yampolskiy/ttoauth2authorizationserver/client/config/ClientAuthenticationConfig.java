package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.authentication.ClientSecretAuthenticationProvider;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.auth.core.service.DatabaseAuthorizationService;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.client.infrastructure.ClientAuthenticationLoggingProvider;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.client.service.SecurityRegisteredClientRepository;

@Configuration
public class ClientAuthenticationConfig {
    @Bean
    public AuthenticationProvider clientAuthenticationProvider(
            SecurityRegisteredClientRepository registeredClientRepository,
            DatabaseAuthorizationService authorizationService,
            PasswordEncoder passwordEncoder
    ) {
        ClientSecretAuthenticationProvider baseProvider =
                new ClientSecretAuthenticationProvider(registeredClientRepository, authorizationService);
        baseProvider.setPasswordEncoder(passwordEncoder);

        return new ClientAuthenticationLoggingProvider(baseProvider);
    }
}
