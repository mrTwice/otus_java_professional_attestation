package ru.otus.java.professional.yampolskiy.ttuserservice.mappers;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public abstract class UserMapper {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", qualifiedByName = "encodePassword")
    @Mapping(target = "emailVerified", constant = "false")
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "locked", constant = "false")
    @Mapping(target = "updatedAtOidc", ignore = true)
    public abstract User toEntityFromUserCreateDTO(UserCreateDTO dto);

    @Mapping(target = "roles", qualifiedByName = "rolesToStringSet")
    public abstract UserResponseDTO toResponseDTOFromEntity(User entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", qualifiedByName = "stringSetToRoles")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    public abstract void updateEntityFromAdminDTO(UserAdminUpdateDTO dto, @MappingTarget User entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    public abstract void updateEntityFromUpdateDTO(UserUpdateDTO dto, @MappingTarget User entity);

    @Mapping(target = "roles", expression = "java(mapRoles(user))")
    @Mapping(target = "permissions", expression = "java(mapPermissions(user))")
    public abstract UserPrincipalDTO toPrincipalDTO(User user);

    protected Set<String> mapRoles(User user) {
        return user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

    protected Set<String> mapPermissions(User user) {
        return user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .collect(Collectors.toSet());
    }

    @Named("rolesToStringSet")
    protected Set<String> mapRoles(Set<Role> roles) {
        return roles != null
                ? roles.stream().map(Role::getName).collect(Collectors.toSet())
                : Collections.emptySet();
    }

    @Named("stringSetToRoles")
    protected Set<Role> mapRoleNamesToRoles(Set<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            return Collections.emptySet();
        }
        return roleNames.stream()
                .map(name -> Role.builder().name(name).build())
                .collect(Collectors.toSet());
    }

    @Named("encodePassword")
    protected String encodePassword(String password) {
        return password != null ? passwordEncoder.encode(password) : null;
    }
}



