package ru.otus.java.professional.yampolskiy.ttuserservice.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.User;
import ru.otus.java.professional.yampolskiy.ttuserservice.exceptions.DuplicateResourceException;
import ru.otus.java.professional.yampolskiy.ttuserservice.repositories.UserRepository;

@Component
@RequiredArgsConstructor
public class UserUniqueValidator {

    private final UserRepository userRepository;

    public void validate(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateResourceException("Username is already taken: " + user.getUsername());
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException("Email is already taken: " + user.getEmail());
        }
    }
}
