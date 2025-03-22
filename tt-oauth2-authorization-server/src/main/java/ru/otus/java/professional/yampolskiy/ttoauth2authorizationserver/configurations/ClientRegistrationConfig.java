package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.configurations;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.repositories.JpaRegisteredClientRepository;

import java.util.UUID;

@Configuration
public class ClientRegistrationConfig {
    @Bean
    public CommandLineRunner registerTestClient(
            JpaRegisteredClientRepository registeredClientRepository,
            PasswordEncoder passwordEncoder,
            TokenSettings tokenSettings) {
        return args -> {
            if (registeredClientRepository.findByClientId("test-client") == null) {
                RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
                        .clientId("test-client")
                        .clientSecret(passwordEncoder.encode("test-secret"))
                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                        .scope("read")
                        .scope("write")
                        .tokenSettings(tokenSettings)
                        .clientSettings(ClientSettings.builder()
                                .requireAuthorizationConsent(false)
                                .build())
                        .build();

                registeredClientRepository.save(client);
            }
        };
    }

    @Bean
    public CommandLineRunner registerOidcClient(
            JpaRegisteredClientRepository registeredClientRepository,
            PasswordEncoder passwordEncoder,
            TokenSettings tokenSettings) {
        return args -> {
            if (registeredClientRepository.findByClientId("oidc-test-client") == null) {
                RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
                        .clientId("oidc-test-client")
                        .clientSecret(passwordEncoder.encode("oidc-secret"))
                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                        .redirectUri("http://127.0.0.1:8080/login/oauth2/code/oidc-client")
                        .scope("openid")
                        .scope("profile")
                        .scope("email")
                        .scope("read")
                        .scope("write")
                        .clientSettings(ClientSettings.builder()
                                .requireAuthorizationConsent(true)
                                .build())
                        .tokenSettings(tokenSettings)
                        .build();

                registeredClientRepository.save(oidcClient);
            }
        };
    }

}
