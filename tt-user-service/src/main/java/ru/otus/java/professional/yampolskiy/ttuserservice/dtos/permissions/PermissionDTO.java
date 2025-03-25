package ru.otus.java.professional.yampolskiy.ttuserservice.dtos.permissions;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionDTO {
    private String name;
    private String description;
}