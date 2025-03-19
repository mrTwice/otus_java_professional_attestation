package ru.otus.java.professional.yampolskiy.ttuserservice.services;

import ru.otus.java.professional.yampolskiy.ttuserservice.entities.User;

import java.util.List;

public interface UserService {

    User createUser(User user);

    User getUserById(Long id);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> getAllActiveUsers();

    User addRoleToUser(Long userId, String roleName);

    User removeRoleFromUser(Long userId, String roleName);

    User updateUser(Long userId, User user);

    void deleteUser(Long userId);
}
