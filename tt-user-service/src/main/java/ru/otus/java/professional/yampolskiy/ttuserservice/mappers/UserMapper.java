package ru.otus.java.professional.yampolskiy.ttuserservice.mappers;

import org.mapstruct.*;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.user.*;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.Role;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.User;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.Permission;


import java.util.Collections;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = RoleMapper.class,
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface UserMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "updatedAtOidc", ignore = true)
    User toEntityFromUserCreateDTO(UserCreateDTO dto);

    @Mapping(target = "roles", qualifiedByName = "rolesToStringSet")
    UserResponseDTO toResponseDTOFromEntity(User entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", qualifiedByName = "stringSetToRoles")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntityFromAdminDTO(UserAdminUpdateDTO dto, @MappingTarget User entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntityFromUpdateDTO(UserUpdateDTO dto, @MappingTarget User entity);

    @Mapping(target = "roles", expression = "java(mapRoles(user))")
    @Mapping(target = "permissions", expression = "java(mapPermissions(user))")
    UserPrincipalDTO toPrincipalDTO(User user);

    default Set<String> mapRoles(User user) {
        return user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

    default Set<String> mapPermissions(User user) {
        return user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .collect(Collectors.toSet());
    }

    @Named("rolesToStringSet")
    default Set<String> mapRoles(Set<Role> roles) {
        return roles != null
                ? roles.stream().map(Role::getName).collect(Collectors.toSet())
                : Collections.emptySet();
    }

    @Named("stringSetToRoles")
    default Set<Role> mapRoleNamesToRoles(Set<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            return Collections.emptySet();
        }
        return roleNames.stream()
                .map(name -> Role.builder().name(name).build())
                .collect(Collectors.toSet());
    }
}



