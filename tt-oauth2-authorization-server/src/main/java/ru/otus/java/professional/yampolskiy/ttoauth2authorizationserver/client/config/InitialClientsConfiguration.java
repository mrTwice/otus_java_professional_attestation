package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.client.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.client.service.SecurityRegisteredClientRepository;

import java.util.Set;
import java.util.UUID;

@Configuration
public class InitialClientsConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(InitialClientsConfiguration.class);

    @Bean
    @DependsOn("entityManagerFactory")
    public CommandLineRunner registerInternalClient(
            SecurityRegisteredClientRepository registeredClientRepository,
            PasswordEncoder passwordEncoder,
            TokenSettings tokenSettings
    ) {
        return args -> {
            if (registeredClientRepository.findByClientId("internal-service-client") == null) {
                LOGGER.info("🔐 Registering internal-service-client...");

                RegisteredClient internalClient = RegisteredClient.withId(UUID.randomUUID().toString())
                        .clientId("internal-service-client")
                        .clientSecret(passwordEncoder.encode("internal-secret"))
                        .clientAuthenticationMethods(authMethods -> {
                            authMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
                            authMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_POST);
                        })
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
    @DependsOn("entityManagerFactory")
    public CommandLineRunner registerSwaggerClient(
            SecurityRegisteredClientRepository registeredClientRepository,
            PasswordEncoder passwordEncoder,
            TokenSettings tokenSettings
    ) {
        return args -> {
            if (registeredClientRepository.findByClientId("swagger-client") == null) {
                LOGGER.info("📚 Registering swagger-client...");

                Set<String> swaggerScopes = Set.of(
                        // OIDC scopes
                        "openid", "profile",

                        // Task access
                        "task:create", "task:view", "task:update", "task:delete", "task:assign",

                        // Comment access
                        "comment:create", "comment:view", "comment:update", "comment:delete",

                        // Attachment access
                        "attachment:create", "attachment:view", "attachment:update", "attachment:delete",

                        // Directory access
                        "task-type:view", "task-status:view", "task-priority:view",

                        // User info
                        "user:view"
                );

                RegisteredClient.Builder builder = RegisteredClient.withId(UUID.randomUUID().toString())
                        .clientId("swagger-client")
                        .clientSecret(passwordEncoder.encode("swagger-secret"))
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                        .redirectUri("http://localhost:8081/swagger-ui/oauth2-redirect.html")
                        .clientSettings(ClientSettings.builder()
                                .requireAuthorizationConsent(true)
                                .build())
                        .tokenSettings(tokenSettings);

                // ✅ Добавляем реальные scopes
                swaggerScopes.forEach(builder::scope);

                registeredClientRepository.save(builder.build());
                LOGGER.info("✅ swagger-client registered successfully");
            }
        };
    }
}
