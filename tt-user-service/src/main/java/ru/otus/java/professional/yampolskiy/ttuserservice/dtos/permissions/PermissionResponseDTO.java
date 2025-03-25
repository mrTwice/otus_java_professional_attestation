package ru.otus.java.professional.yampolskiy.ttuserservice.dtos.permissions;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionResponseDTO {
    private UUID id;
    private String name;
    private String description;
}