package ru.otus.java.professional.yampolskiy.ttuserservice.dtos.user;

import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {
    private UUID id;
    private String username;
    private String email;
    private boolean emailVerified;
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
    private Instant updatedAtOidc;
    private Map<String, Object> address;
    private boolean active;
    private boolean locked;
    private Instant credentialsExpireAt;
    private Instant accountExpireAt;
    private Set<String> roles;
}
