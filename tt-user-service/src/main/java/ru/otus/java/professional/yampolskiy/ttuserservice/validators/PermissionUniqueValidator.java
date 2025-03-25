package ru.otus.java.professional.yampolskiy.ttuserservice.validators;

import lombok.RequiredArgsConstructor;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.Permission;
import ru.otus.java.professional.yampolskiy.ttuserservice.exceptions.DuplicateResourceException;
import ru.otus.java.professional.yampolskiy.ttuserservice.repositories.PermissionRepository;

@RequiredArgsConstructor
public class PermissionUniqueValidator implements Validator<Permission> {

    private final PermissionRepository permissionRepository;

    @Override
    public void validate(Permission permission) {
        if (permissionRepository.existsByName(permission.getName())) {
            throw new DuplicateResourceException("Permission with name '" + permission.getName() + "' already exists");
        }
    }
}