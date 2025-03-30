package ru.otus.java.professional.yampolskiy.ttuserservice.entities;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.*;
import ru.otus.java.professional.yampolskiy.ttuserservice.converters.JsonMapConverter;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class User {

    // 🔐 Идентификаторы
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "username", unique = true, nullable = false)
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @Column(name = "password", nullable = false)
    @NotBlank
    private String password;

    @Column(name = "email", unique = true, nullable = false)
    @Email
    @NotBlank
    private String email;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @Column(name = "is_locked", nullable = false)
    private boolean locked = false;

    @Column(name = "credentials_expire_at")
    private Instant credentialsExpireAt;

    @Column(name = "account_expire_at")
    private Instant accountExpireAt;

    // 🧾 Основная информация
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "locale")
    private String locale;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Column(name = "zoneinfo")
    private String zoneinfo;

    // 📞 Контакты
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "phone_number_verified", nullable = false)
    private boolean phoneNumberVerified;

    // 🌐 Ссылки
    @Column(name = "profile")
    private URL profile;

    @Column(name = "website")
    private URL website;

    @Column(name = "picture_url")
    private URL pictureUrl;

    // 📦 Адрес (JSON)
    @Column(name = "address", columnDefinition = "jsonb")
    @Convert(converter = JsonMapConverter.class)
    @ColumnTransformer(write = "?::jsonb")
    private Map<String, Object> address;

    // 🧾 OIDC Provider Info (обязательные, если все пользователи идут через SAS)
    @Column(name = "oidc_provider", nullable = false)
    private String oidcProvider;

    @Column(name = "oidc_subject", unique = true, nullable = false)
    private UUID oidcSubject;

    @Column(name = "updated_at_oidc")
    private Instant updatedAtOidc;

    // 🕓 Аудит
    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // 🔐 Роли и привилегии
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}

