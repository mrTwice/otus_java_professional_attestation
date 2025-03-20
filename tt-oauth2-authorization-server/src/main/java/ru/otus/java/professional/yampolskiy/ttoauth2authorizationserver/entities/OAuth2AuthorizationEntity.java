package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "oauth2_authorization",
        indexes = {
                @Index(name = "idx_oauth2_authorization_principal_name", columnList = "principalName"),
                @Index(name = "idx_oauth2_authorization_grant_type", columnList = "authorizationGrantType"),
                @Index(name = "idx_oauth2_authorization_access_token", columnList = "accessTokenValue", unique = true),
                @Index(name = "idx_oauth2_authorization_refresh_token", columnList = "refreshTokenValue", unique = true)
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuth2AuthorizationEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String registeredClientId;

    @Column(nullable = false)
    private String principalName;

    @Column(nullable = false)
    private String authorizationGrantType;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String authorizedScopes;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String attributes;

    private String state;

    // Authorization Code
    @Lob
    @Column(columnDefinition = "TEXT")
    private String authorizationCodeValue;

    private Instant authorizationCodeIssuedAt;
    private Instant authorizationCodeExpiresAt;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String authorizationCodeMetadata;

    // Access Token
    @Lob
    @Column(columnDefinition = "TEXT", unique = true)
    private String accessTokenValue;

    private Instant accessTokenIssuedAt;
    private Instant accessTokenExpiresAt;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String accessTokenMetadata;

    private String accessTokenType;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String accessTokenScopes;

    // Refresh Token
    @Lob
    @Column(columnDefinition = "TEXT", unique = true)
    private String refreshTokenValue;

    private Instant refreshTokenIssuedAt;
    private Instant refreshTokenExpiresAt;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String refreshTokenMetadata;

}