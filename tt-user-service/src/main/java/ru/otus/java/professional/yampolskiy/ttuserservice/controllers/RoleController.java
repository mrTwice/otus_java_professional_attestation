package ru.otus.java.professional.yampolskiy.ttuserservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.role.RoleDTO;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.role.RoleResponseDTO;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.Role;
import ru.otus.java.professional.yampolskiy.ttuserservice.mappers.RoleMapper;
import ru.otus.java.professional.yampolskiy.ttuserservice.services.RoleService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final RoleMapper roleMapper;

    @PreAuthorize("@accessPolicy.canAdminUpdateUsers(authentication)")
    @PostMapping
    public ResponseEntity<RoleResponseDTO> createRole(@RequestBody @Valid RoleDTO dto) {
        Role role = roleMapper.toEntityFromRoleDTO(dto);
        Role created = roleService.createRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleMapper.toResponseDTOFromRole(created));
    }

    @PreAuthorize("@accessPolicy.canViewRoles(authentication)")
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> getRoleById(@PathVariable UUID id) {
        Role role = roleService.getRoleById(id);
        return ResponseEntity.ok(roleMapper.toResponseDTOFromRole(role));
    }

    @PreAuthorize("@accessPolicy.canViewRoles(authentication)")
    @GetMapping("/by-name/{name}")
    public ResponseEntity<RoleResponseDTO> getRoleByName(@PathVariable String name) {
        Role role = roleService.getRoleByName(name);
        return ResponseEntity.ok(roleMapper.toResponseDTOFromRole(role));
    }

    @PreAuthorize("@accessPolicy.canViewRoles(authentication)")
    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        List<RoleResponseDTO> dtos = roles.stream()
                .map(roleMapper::toResponseDTOFromRole)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("@accessPolicy.canAdminUpdateUsers(authentication)")
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> updateRole(@PathVariable UUID id, @RequestBody @Valid RoleDTO dto) {
        Role role = roleMapper.toEntityFromRoleDTO(dto);
        Role updated = roleService.updateRole(id, role);
        return ResponseEntity.ok(roleMapper.toResponseDTOFromRole(updated));
    }

    @PreAuthorize("@accessPolicy.canAdminUpdateUsers(authentication)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable UUID id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}