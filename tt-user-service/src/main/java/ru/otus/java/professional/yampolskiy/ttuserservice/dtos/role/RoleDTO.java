package ru.otus.java.professional.yampolskiy.ttuserservice.dtos.role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.permissions.PermissionDTO;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private String name;
    private Set<PermissionDTO> permissions;
}