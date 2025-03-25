package ru.otus.java.professional.yampolskiy.ttuserservice.mappers;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.user.*;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.Role;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.User;
import ru.otus.java.professional.yampolskiy.ttuserservice.repositories.RoleRepository;
import ru.otus.java.professional.yampolskiy.ttuserservice.services.RoleService;
import ru.otus.java.professional.yampolskiy.ttuserservice.services.RoleServiceImpl;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = RoleMapper.class)
public abstract class UserMapper {

     @Autowired
    protected PasswordEncoder passwordEncoder;

    @Mapping(target = "id", ignore = true) // ID генерируется БД
    @Mapping(target = "createdAt", ignore = true) // Управляется Hibernate
    @Mapping(target = "updatedAt", ignore = true) // Управляется Hibernate
    @Mapping(target = "deletedAt", ignore = true) // Управляется Hibernate
    @Mapping(target = "password", qualifiedByName = "encodePassword")
    @Mapping(target = "roles", ignore = true) // Роли мапятся отдельно
    public abstract User toEntityFromUserCreateDTO(UserCreateDTO dto);

    @Mapping(target = "roles", qualifiedByName = "rolesToStringSet")
    public abstract UserResponseDTO toResponseDTOFromEntity(User entity);

    @Mapping(target = "id", ignore = true) // ID не обновляем
    @Mapping(target = "password", ignore = true) // Пароль не меняем в этом методе
    @Mapping(target = "roles", ignore = true) // Роли отдельно обновляются
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    public abstract void updateEntityFromUpdateDTO(UserUpdateDTO dto, @MappingTarget User entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "username", ignore = true) // Админ не должен менять username
    @Mapping(target = "email", ignore = true) // Email тоже фиксированный
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "roles", qualifiedByName = "stringSetToRoles")
    public abstract void updateEntityFromAdminDTO(UserAdminUpdateDTO dto, @MappingTarget User entity);

    @Named("rolesToStringSet")
    protected Set<String> mapRoles(Set<Role> roles) {
        return roles != null ? roles.stream().map(Role::getName).collect(Collectors.toSet()) : Collections.emptySet();
    }

    @Named("encodePassword")
    protected String encodePassword(String password) {
        return password != null ? passwordEncoder.encode(password) : null;
    }

    @Named("stringSetToRoles")
    protected Set<Role> mapRoleNamesToRoles(Set<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            return Collections.emptySet();
        }
        return roleNames.stream().map(roleName -> {
            Role role = new Role();
            role.setName(roleName);
            return role;
        }).collect(Collectors.toSet());
    }
}


