package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

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