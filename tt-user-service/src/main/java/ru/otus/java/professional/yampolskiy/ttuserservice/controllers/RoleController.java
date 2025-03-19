package ru.otus.java.professional.yampolskiy.ttuserservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.RoleDTO;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.RoleResponseDTO;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.Role;
import ru.otus.java.professional.yampolskiy.ttuserservice.mappers.RoleMapper;
import ru.otus.java.professional.yampolskiy.ttuserservice.services.RoleService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final RoleMapper roleMapper;

    @PostMapping
    public ResponseEntity<RoleResponseDTO> createRole(@RequestBody RoleDTO roleDTO) {
        Role role = roleMapper.toEntity(roleDTO);
        Role createdRole = roleService.createRole(role);
        RoleResponseDTO responseDTO = roleMapper.toResponseDTO(createdRole);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> getRoleById(@PathVariable Long id) {
        Role role = roleService.getRoleById(id);
        RoleResponseDTO responseDTO = roleMapper.toResponseDTO(role);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<RoleResponseDTO> getRoleByName(@PathVariable String name) {
        Role role = roleService.getRoleByName(name);
        RoleResponseDTO responseDTO = roleMapper.toResponseDTO(role);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        List<RoleResponseDTO> responseDTOs = roles.stream()
                .map(roleMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> updateRole(
            @PathVariable Long id,
            @RequestBody RoleDTO roleDTO
    ) {
        Role role = roleMapper.toEntity(roleDTO);
        Role updatedRole = roleService.updateRole(id, role);
        RoleResponseDTO responseDTO = roleMapper.toResponseDTO(updatedRole);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}