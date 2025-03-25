package ru.otus.java.professional.yampolskiy.ttuserservice.dtos.user;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAdminUpdateDTO {
    private boolean isActive;
    private boolean emailVerified;
    private Set<String> roles;
}