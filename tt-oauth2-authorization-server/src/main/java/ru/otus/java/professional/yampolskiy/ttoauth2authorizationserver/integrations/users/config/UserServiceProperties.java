package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.integrations.users.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "user-service.auth")
public class UserServiceProperties {
    private String clientId;
    private String clientSecret;
    private List<String> scopes;
}
