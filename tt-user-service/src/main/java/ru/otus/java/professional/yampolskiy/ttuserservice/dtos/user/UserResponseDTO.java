package ru.otus.java.professional.yampolskiy.ttuserservice.dtos.user;

import lombok.*;

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
    private String lastName;
    private String pictureUrl;
    private String locale;
    private boolean isActive;
    private Set<String> roles;
}
