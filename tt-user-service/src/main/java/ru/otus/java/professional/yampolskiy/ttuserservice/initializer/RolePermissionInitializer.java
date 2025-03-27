package ru.otus.java.professional.yampolskiy.ttuserservice.initializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.Permission;
import ru.otus.java.professional.yampolskiy.ttuserservice.services.PermissionService;
import ru.otus.java.professional.yampolskiy.ttuserservice.services.RoleService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
public class RolePermissionInitializer implements CommandLineRunner {

    private final PermissionService permissionService;
    private final RoleService roleService;

    @Override
    public void run(String... args) {
        log.info("🔐 Initializing default roles and permissions...");

        // TODO: Use enums for roles and permissions
        Map<String, Set<String>> rolePermissions = Map.of(
                "ADMIN", Set.of(
                        "task:create", "task:view", "task:update", "task:delete", "task:assign",
                        "project:view", "project:manage",

                        "user:view", "user:update", "user:delete", "user:assign-roles", "user:manage",

                        "role:create", "role:view", "role:update", "role:delete",
                        "permission:create", "permission:view", "permission:update", "permission:delete"
                ),

                "MANAGER", Set.of(
                        "task:create", "task:view", "task:update", "task:delete", "task:assign",
                        "project:view", "project:manage",

                        "user:view", "user:update",

                        "role:view", "permission:view"
                ),

                "USER", Set.of(
                        "task:create", "task:view", "task:update",
                        "user:view"
                ),

                "GUEST", Set.of(
                        "task:view", "project:view"
                )
        );

        // Создание пермишенов
        Map<String, Permission> createdPermissions = new HashMap<>();
        Set<String> allPermissions = rolePermissions.values().stream()
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        for (String name : allPermissions) {
            Permission permission;
            if (permissionService.existsByName(name)) {
                permission = permissionService.getPermissionByName(name);
                log.debug("✅ Permission '{}' already exists", name);
            } else {
                permission = permissionService.createPermission(
                        Permission.builder()
                                .name(name)
                                .description("Allows " + name.replace(":", " ") + " action")
                                .build()
                );
                log.info("➕ Created permission: {}", name);
            }
            createdPermissions.put(name, permission);
        }

        // Создание ролей
        for (Map.Entry<String, Set<String>> entry : rolePermissions.entrySet()) {
            String roleName = entry.getKey();
            Set<Permission> permissions = entry.getValue().stream()
                    .map(createdPermissions::get)
                    .collect(Collectors.toSet());

            roleService.createOrUpdateRoleWithPermissions(roleName, permissions);
            log.info("✅ Role '{}' initialized with permissions: {}", roleName,
                    permissions.stream().map(Permission::getName).collect(Collectors.joining(", "))
            );
        }

        log.info("🎉 Default roles and permissions initialized successfully.");
    }
}
