package ru.otus.java.professional.yampolskiy.ttuserservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.UpdateUserDTO;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.UserDTO;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.UserResponseDTO;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.Role;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.User;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", qualifiedByName = "encodePassword")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "stringSetToRoles")
    public abstract User toEntity(UserDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "roles", ignore = true)
    public abstract User toEntity(UpdateUserDTO dto);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToStringSet")
    public abstract UserResponseDTO toResponseDTO(User user);

    @Named("stringSetToRoles")
    protected Set<Role> mapStringSetToRoles(Set<String> roleNames) {
        return roleNames == null ? Collections.emptySet() :
                roleNames.stream().map(Role::new).collect(Collectors.toSet());
    }

    @Named("rolesToStringSet")
    protected Set<String> mapRoles(Set<Role> roles) {
        return roles.stream().map(Role::getName).collect(Collectors.toSet());
    }

    @Named("encodePassword")
    protected String encodePassword(String password) {
        return password != null ? passwordEncoder.encode(password) : null;
    }
}


