package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.auth.core.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

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
public class Oauth2Authorization {

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
    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    private String authorizedScopes;

    @Lob
    @Column(columnDefinition = "TEXT")
    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    private String attributes;

    private String state;

    // Authorization Code
    @Lob
    @Column(columnDefinition = "TEXT")
    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    private String authorizationCodeValue;

    private Instant authorizationCodeIssuedAt;
    private Instant authorizationCodeExpiresAt;

    @Lob
    @Column(columnDefinition = "TEXT")
    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    private String authorizationCodeMetadata;

    // Access Token
    @Lob
    @Column(columnDefinition = "TEXT", unique = true)
    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    private String accessTokenValue;

    private Instant accessTokenIssuedAt;
    private Instant accessTokenExpiresAt;

    @Lob
    @Column(columnDefinition = "TEXT")
    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    private String accessTokenMetadata;

    private String accessTokenType;

    @Lob
    @Column(columnDefinition = "TEXT")
    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    private String accessTokenScopes;

    // Refresh Token
    @Lob
    @Column(columnDefinition = "TEXT", unique = true)
    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    private String refreshTokenValue;

    private Instant refreshTokenIssuedAt;
    private Instant refreshTokenExpiresAt;

    @Lob
    @Column(columnDefinition = "TEXT")
    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    private String refreshTokenMetadata;

    // ID Token (OIDC)
    @Lob
    @Column(columnDefinition = "TEXT")
    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    private String idTokenValue;

    private Instant idTokenIssuedAt;
    private Instant idTokenExpiresAt;

    @Lob
    @Column(columnDefinition = "TEXT")
    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    private String idTokenMetadata;

}