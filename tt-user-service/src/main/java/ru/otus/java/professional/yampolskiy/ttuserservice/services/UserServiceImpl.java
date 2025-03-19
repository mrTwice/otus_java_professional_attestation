package ru.otus.java.professional.yampolskiy.ttuserservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.Role;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.User;
import ru.otus.java.professional.yampolskiy.ttuserservice.exceptions.DuplicateResourceException;
import ru.otus.java.professional.yampolskiy.ttuserservice.exceptions.ResourceNotFoundException;
import ru.otus.java.professional.yampolskiy.ttuserservice.repositories.RoleRepository;
import ru.otus.java.professional.yampolskiy.ttuserservice.repositories.UserRepository;
import ru.otus.java.professional.yampolskiy.ttuserservice.validators.CommonUserValidator;
import ru.otus.java.professional.yampolskiy.ttuserservice.validators.UserEmailValidator;
import ru.otus.java.professional.yampolskiy.ttuserservice.validators.UserUniqueValidator;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CommonUserValidator commonUserValidator;
    private final UserEmailValidator userEmailValidator;
    private final UserUniqueValidator userUniqueValidator;

    @Override
    public User createUser(User user) {
        commonUserValidator.validate(user);
        userEmailValidator.validate(user.getEmail());
        userUniqueValidator.validate(user);
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<User> getAllActiveUsers() {
        return userRepository.findAllByIsActiveTrue();
    }

    @Override
    public User addRoleToUser(Long userId, String roleName) {
        User user = getUserById(userId);
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + roleName));
        user.getRoles().add(role);
        return userRepository.save(user);
    }

    @Override
    public User removeRoleFromUser(Long userId, String roleName) {
        User user = getUserById(userId);
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + roleName));
        user.getRoles().remove(role);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long userId, User user) {
        User existingUser = getUserById(userId);
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setActive(user.isActive());
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}
