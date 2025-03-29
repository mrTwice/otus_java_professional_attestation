package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.oidc.dto;

import lombok.*;

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
    private String passwordHash;
    private boolean emailVerified;
    private boolean active;
    private String firstName;
    private String lastName;
    private String locale;
    private Set<String> roles;
    private Set<String> permissions;
}