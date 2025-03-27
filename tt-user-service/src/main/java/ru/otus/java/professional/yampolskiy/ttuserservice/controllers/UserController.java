package ru.otus.java.professional.yampolskiy.ttuserservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.user.*;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.User;
import ru.otus.java.professional.yampolskiy.ttuserservice.exceptions.ResourceNotFoundException;
import ru.otus.java.professional.yampolskiy.ttuserservice.mappers.UserMapper;
import ru.otus.java.professional.yampolskiy.ttuserservice.services.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    @PreAuthorize("@accessPolicy.canCreateUsers(authentication)")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserCreateDTO dto) {
        User user = userMapper.toEntityFromUserCreateDTO(dto);
        User created = userService.createUser(user);
        return new ResponseEntity<>(userMapper.toResponseDTOFromEntity(created), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@accessPolicy.canAccessUser(authentication, #id)")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable UUID id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(userMapper.toResponseDTOFromEntity(user));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@accessPolicy.canUpdateUser(authentication, #id)")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id,
                                                      @RequestBody @Valid UserUpdateDTO dto) {
        User updated = userService.updateUser(id, userMapper.toEntityFromUserCreateDTO(
                UserCreateDTO.builder()
                        .username(dto.getUsername())
                        .email(dto.getEmail())
                        .firstName(dto.getFirstName())
                        .lastName(dto.getLastName())
                        .locale(dto.getLocale())
                        .pictureUrl(dto.getPictureUrl())
                        .password("placeholder")
                        .build()
        ));
        return ResponseEntity.ok(userMapper.toResponseDTOFromEntity(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@accessPolicy.canDeleteUsers(authentication)")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("@accessPolicy.canViewUsers(authentication)")
    public ResponseEntity<List<UserResponseDTO>> getAllActiveUsers() {
        List<User> users = userService.getAllActiveUsers();
        List<UserResponseDTO> dtos = users.stream()
                .map(userMapper::toResponseDTOFromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/{id}/password")
    @PreAuthorize("@accessPolicy.canChangePasswordFor(authentication, #id)")
    public ResponseEntity<Void> updatePasswordHash(@PathVariable UUID id,
                                                   @RequestBody @Valid PasswordHashUpdateDTO dto) {
        userService.updatePasswordHash(id, dto.getPasswordHash());
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{id}/admin")
    @PreAuthorize("@accessPolicy.canAdminUpdateUsers(authentication)")
    public ResponseEntity<UserResponseDTO> adminUpdate(@PathVariable UUID id,
                                                       @RequestBody @Valid UserAdminUpdateDTO dto) {
        User user = userService.getUserById(id);
        userMapper.updateEntityFromAdminDTO(dto, user);
        User updated = userService.createUser(user);
        return ResponseEntity.ok(userMapper.toResponseDTOFromEntity(updated));
    }

    @PatchMapping("/{id}/roles/add")
    @PreAuthorize("@accessPolicy.canAssignRoles(authentication)")
    public ResponseEntity<UserResponseDTO> addRole(@PathVariable UUID id,
                                                   @RequestParam String roleName) {
        User updated = userService.addRoleToUser(id, roleName);
        return ResponseEntity.ok(userMapper.toResponseDTOFromEntity(updated));
    }

    @PatchMapping("/{id}/roles/remove")
    @PreAuthorize("@accessPolicy.canAssignRoles(authentication)")
    public ResponseEntity<UserResponseDTO> removeRole(@PathVariable UUID id,
                                                      @RequestParam String roleName) {
        User updated = userService.removeRoleFromUser(id, roleName);
        return ResponseEntity.ok(userMapper.toResponseDTOFromEntity(updated));
    }

    @GetMapping("/by-subject")
    @PreAuthorize("@accessPolicy.canViewUsers(authentication)")
    public ResponseEntity<UserPrincipalDTO> getUserBySubject(@RequestParam String subject) {
        User user = userService.getUserByOidcSubject(subject);
        return ResponseEntity.ok(userMapper.toPrincipalDTO(user));
    }

    @GetMapping("/exists")
    @PreAuthorize("@accessPolicy.canViewUsers(authentication)")
    public ResponseEntity<Boolean> exists(@RequestParam String subject) {
        return ResponseEntity.ok(userService.existsByOidcSubject(subject));
    }

    @GetMapping("/by-provider")
    @PreAuthorize("@accessPolicy.canViewUsers(authentication)")
    public ResponseEntity<List<UserPrincipalDTO>> getUsersByProvider(@RequestParam String provider) {
        List<User> users = userService.getUsersByOidcProvider(provider);
        return ResponseEntity.ok(users.stream()
                .map(userMapper::toPrincipalDTO)
                .toList());
    }

    @GetMapping("/profile")
    @PreAuthorize("@accessPolicy.canViewUsers(authentication)")
    public ResponseEntity<UserPrincipalDTO> getPrincipalByUsername(@RequestParam String username) {
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(userMapper.toPrincipalDTO(user));
    }

    @GetMapping("/auth/profile/oidc")
    @PreAuthorize("@accessPolicy.canViewUsers(authentication)")
    public ResponseEntity<UserPrincipalDTO> getPrincipalByOidcAndProvider(@RequestParam String provider, @RequestParam String subject) {
        User user = userService.getUserByOidcSubjectAndProvider(subject, provider);
        return ResponseEntity.ok(userMapper.toPrincipalDTO(user));
    }
}
