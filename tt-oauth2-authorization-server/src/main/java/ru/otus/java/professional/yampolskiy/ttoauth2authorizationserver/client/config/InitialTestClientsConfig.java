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
public class InitialTestClientsConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitialTestClientsConfig.class);

    @Bean
    @DependsOn("entityManagerFactory")
    public CommandLineRunner registerTestClient(
            SecurityRegisteredClientRepository registeredClientRepository,
            PasswordEncoder passwordEncoder,
            TokenSettings tokenSettings
    ) {
        return args -> {
            if (registeredClientRepository.findByClientId("test-client") == null) {
                LOGGER.info("📦 Registering test-client");

                // 🧠 Все возможные permission'ы (собраны вручную или из RolePermissionInitializer)
                Set<String> allScopes = Set.of(
                        "openid", "profile", "email", "read", "write", "offline_access",

                        "task:create", "task:view", "task:update", "task:delete", "task:assign",
                        "project:view", "project:manage",

                        "user:view", "user:update", "user:delete", "user:assign-roles", "user:manage",

                        "role:create", "role:view", "role:update", "role:delete",
                        "permission:create", "permission:view", "permission:update", "permission:delete"
                );

                RegisteredClient.Builder builder = RegisteredClient.withId(UUID.randomUUID().toString())
                        .clientId("test-client")
                        .clientSecret(passwordEncoder.encode("test-secret"))
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                        .redirectUri("http://127.0.0.1:8080/login/oauth2/code/test-client")
                        .clientSettings(ClientSettings.builder()
                                .requireAuthorizationConsent(true)
                                .build())
                        .tokenSettings(tokenSettings);

                // 🔐 Добавляем все scope'ы
                allScopes.forEach(builder::scope);

                registeredClientRepository.save(builder.build());
                LOGGER.info("✅ test-client registered successfully with full scopes");
            }
        };
    }

}
