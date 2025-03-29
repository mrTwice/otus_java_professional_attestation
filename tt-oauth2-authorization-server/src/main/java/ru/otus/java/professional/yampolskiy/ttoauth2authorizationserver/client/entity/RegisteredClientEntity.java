package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.client.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.common.util.JsonListConverter;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.common.util.JsonMapConverter;

import java.time.Instant;
import java.util.List;
import java.util.Map;

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
public class RegisteredClientEntity {

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

    @Convert(converter = JsonListConverter.class)
    @Column(nullable = false, columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private List<String> clientAuthenticationMethods;

    @Convert(converter = JsonListConverter.class)
    @Column(nullable = false, columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private List<String> authorizationGrantTypes;

    @Convert(converter = JsonListConverter.class)
    @Column(columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private List<String> redirectUris;

    @Convert(converter = JsonListConverter.class)
    @Column(nullable = false, columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private List<String> scopes;

    @Convert(converter = JsonMapConverter.class)
    @Column(nullable = false, columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private Map<String, Object> clientSettings;

    @Convert(converter = JsonMapConverter.class)
    @Column(nullable = false, columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private Map<String, Object> tokenSettings;
}