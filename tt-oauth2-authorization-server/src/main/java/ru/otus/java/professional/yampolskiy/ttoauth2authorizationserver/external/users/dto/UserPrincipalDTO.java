package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.external.users.dto;

import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPrincipalDTO {
    private UUID id;
    private String username;
    private String email;
    private String password;
    private boolean emailVerified;
    private boolean active;
    private boolean locked;
    private Instant credentialsExpireAt;
    private Instant accountExpireAt;
    private String firstName;
    private String middleName;
    private String lastName;
    private String nickname;
    private String pictureUrl;
    private String profile;
    private String website;
    private String locale;
    private String gender;
    private LocalDate birthdate;
    private String zoneinfo;
    private String phoneNumber;
    private boolean phoneNumberVerified;
    private UUID oidcSubject;
    private String oidcProvider;
    private Instant updatedAtOidc;
    private Map<String, Object> address;

    private Set<String> roles;
    private Set<String> permissions;
}