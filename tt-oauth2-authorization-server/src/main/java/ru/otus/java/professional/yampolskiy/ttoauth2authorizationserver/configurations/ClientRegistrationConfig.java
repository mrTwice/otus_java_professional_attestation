package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.configurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.repositories.JpaRegisteredClientRepository;

import java.util.UUID;

@Configuration
public class ClientRegistrationConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientRegistrationConfig.class);
    @Bean
    @DependsOn("entityManagerFactory")
    public CommandLineRunner registerTestClient(
            JpaRegisteredClientRepository registeredClientRepository,
            PasswordEncoder passwordEncoder,
            TokenSettings tokenSettings) {
        return args -> {
            if (registeredClientRepository.findByClientId("test-client") == null) {
                LOGGER.info("Saving test-client");
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
    @DependsOn("entityManagerFactory")
    public CommandLineRunner registerOidcClient(
            JpaRegisteredClientRepository registeredClientRepository,
            PasswordEncoder passwordEncoder,
            TokenSettings tokenSettings
    ) {
        return args -> {
            if (registeredClientRepository.findByClientId("oidc-test-client") == null) {
                LOGGER.info("Saving oidc-test-client");
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
                        .scope("offline_access")
                        .clientSettings(ClientSettings.builder()
                                .requireAuthorizationConsent(true)
                                .build())
                        .tokenSettings(tokenSettings)
                        .build();
                LOGGER.info("Registered Client: {}, Grant Types: {}", oidcClient.getClientId(), oidcClient.getAuthorizationGrantTypes());
                registeredClientRepository.save(oidcClient);

            }

        };
    }

    @Bean
    @DependsOn("entityManagerFactory")
    public CommandLineRunner registerInternalClient(
            JpaRegisteredClientRepository registeredClientRepository,
            PasswordEncoder passwordEncoder,
            TokenSettings tokenSettings
    ) {
        return args -> {
            if (registeredClientRepository.findByClientId("internal-service-client") == null) {
                LOGGER.info("🔐 Registering internal-service-client...");

                RegisteredClient internalClient = RegisteredClient.withId(UUID.randomUUID().toString())
                        .clientId("internal-service-client")
                        .clientSecret(passwordEncoder.encode("internal-secret"))
                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                        .scope("user:view")
                        .scope("user:manage")
                        .scope("user:delete")
                        .tokenSettings(tokenSettings)
                        .clientSettings(ClientSettings.builder()
                                .requireAuthorizationConsent(false)
                                .build())
                        .build();

                registeredClientRepository.save(internalClient);
                LOGGER.info("✅ internal-service-client registered");
            }
        };
    }

    @Bean
    public UserDetailsService userDetailsService(
            PasswordEncoder passwordEncoder
    ) {
        UserDetails user = User.withUsername("test-user")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

}
