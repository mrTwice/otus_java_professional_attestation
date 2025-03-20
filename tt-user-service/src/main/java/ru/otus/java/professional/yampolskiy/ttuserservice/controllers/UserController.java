package ru.otus.java.professional.yampolskiy.ttuserservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.UpdateUserDTO;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.UserDTO;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.UserResponseDTO;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.User;
import ru.otus.java.professional.yampolskiy.ttuserservice.mappers.UserMapper;
import ru.otus.java.professional.yampolskiy.ttuserservice.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        User createdUser = userService.createUser(user);
        UserResponseDTO responseDTO = userMapper.toResponseDTO(createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        UserResponseDTO responseDTO = userMapper.toResponseDTO(user);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        UserResponseDTO responseDTO = userMapper.toResponseDTO(user);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        UserResponseDTO responseDTO = userMapper.toResponseDTO(user);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/active")
    public ResponseEntity<List<UserResponseDTO>> getAllActiveUsers() {
        List<User> users = userService.getAllActiveUsers();
        List<UserResponseDTO> responseDTOs = users.stream()
                .map(userMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(responseDTOs);
    }

    @PostMapping("/{userId}/roles")
    public ResponseEntity<UserResponseDTO> addRoleToUser(
            @PathVariable Long userId,
            @RequestParam String roleName
    ) {
        User user = userService.addRoleToUser(userId, roleName);
        UserResponseDTO responseDTO = userMapper.toResponseDTO(user);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{userId}/roles")
    public ResponseEntity<UserResponseDTO> removeRoleFromUser(
            @PathVariable Long userId,
            @RequestParam String roleName
    ) {
        User user = userService.removeRoleFromUser(userId, roleName);
        UserResponseDTO responseDTO = userMapper.toResponseDTO(user);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserDTO updateUserDTO
    ) {
        User user = userMapper.toEntity(updateUserDTO);
        User updatedUser = userService.updateUser(id, user);
        UserResponseDTO responseDTO = userMapper.toResponseDTO(updatedUser);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
