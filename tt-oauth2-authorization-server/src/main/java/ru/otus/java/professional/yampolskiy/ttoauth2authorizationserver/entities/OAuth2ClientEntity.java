package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.entities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "oauth2_client",
        indexes = {
                @Index(name = "idx_oauth2_client_client_id", columnList = "clientId", unique = true)
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuth2ClientEntity {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String clientId;

    @Column(nullable = false)
    private Instant clientIdIssuedAt;

    private String clientSecret;
    private Instant clientSecretExpiresAt;

    @Column(nullable = false)
    private String clientName;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String clientAuthenticationMethods;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String authorizationGrantTypes;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String redirectUris;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String scopes;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String clientSettings;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String tokenSettings;
}