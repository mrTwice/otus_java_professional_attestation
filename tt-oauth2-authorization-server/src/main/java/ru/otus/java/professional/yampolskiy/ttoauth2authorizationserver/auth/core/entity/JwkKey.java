package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.auth.core.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "jwk_keys")
@Data
@NoArgsConstructor
public class JwkKey {

    @Id
    @Column(columnDefinition = "UUID")
    private UUID kid;  // Используем UUID вместо String

    @Column(name = "key_data", columnDefinition = "TEXT", nullable = false)
    private String keyData;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "is_primary", nullable = false)
    private boolean isPrimary;


}