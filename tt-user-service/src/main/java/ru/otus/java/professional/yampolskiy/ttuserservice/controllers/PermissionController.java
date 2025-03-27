package ru.otus.java.professional.yampolskiy.ttuserservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.permissions.PermissionDTO;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.permissions.PermissionResponseDTO;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.Permission;
import ru.otus.java.professional.yampolskiy.ttuserservice.mappers.PermissionMapper;
import ru.otus.java.professional.yampolskiy.ttuserservice.services.PermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;
    private final PermissionMapper permissionMapper;

    @PreAuthorize("@accessPolicy.canManagePermissions(authentication)")
    @PostMapping
    public ResponseEntity<PermissionResponseDTO> createPermission(@RequestBody @Valid PermissionDTO dto) {
        Permission permission = permissionMapper.toEntityFromPermissionDTO(dto);
        Permission saved = permissionService.createPermission(permission);
        return ResponseEntity.status(HttpStatus.CREATED).body(permissionMapper.toResponseDTOFromEntity(saved));
    }

    @PreAuthorize("@accessPolicy.canViewRoles(authentication)")
    @GetMapping("/{id}")
    public ResponseEntity<PermissionResponseDTO> getPermissionById(@PathVariable UUID id) {
        Permission permission = permissionService.getPermissionById(id);
        return ResponseEntity.ok(permissionMapper.toResponseDTOFromEntity(permission));
    }

    @PreAuthorize("@accessPolicy.canViewRoles(authentication)")
    @GetMapping("/by-name/{name}")
    public ResponseEntity<PermissionResponseDTO> getPermissionByName(@PathVariable String name) {
        Permission permission = permissionService.getPermissionByName(name);
        return ResponseEntity.ok(permissionMapper.toResponseDTOFromEntity(permission));
    }

    @PreAuthorize("@accessPolicy.canViewRoles(authentication)")
    @GetMapping
    public ResponseEntity<List<PermissionResponseDTO>> getAllPermissions() {
        List<Permission> permissions = permissionService.getAllPermissions();
        List<PermissionResponseDTO> dtos = permissions.stream()
                .map(permissionMapper::toResponseDTOFromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("@accessPolicy.canManagePermissions(authentication)")
    @PutMapping("/{id}")
    public ResponseEntity<PermissionResponseDTO> updatePermission(@PathVariable UUID id, @RequestBody @Valid PermissionDTO dto) {
        Permission toUpdate = permissionMapper.toEntityFromPermissionDTO(dto);
        Permission updated = permissionService.updatePermission(id, toUpdate);
        return ResponseEntity.ok(permissionMapper.toResponseDTOFromEntity(updated));
    }

    @PreAuthorize("@accessPolicy.canManagePermissions(authentication)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable UUID id) {
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }
}



