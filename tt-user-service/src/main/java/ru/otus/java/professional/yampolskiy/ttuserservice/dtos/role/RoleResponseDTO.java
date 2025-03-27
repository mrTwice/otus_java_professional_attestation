package ru.otus.java.professional.yampolskiy.ttuserservice.dtos.role;

import lombok.*;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.permissions.PermissionDTO;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponseDTO {
    private UUID id;
    private String name;
    private Set<PermissionDTO> permissions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}