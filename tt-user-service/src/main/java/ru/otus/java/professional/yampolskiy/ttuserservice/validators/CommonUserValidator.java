package ru.otus.java.professional.yampolskiy.ttuserservice.validators;

import lombok.RequiredArgsConstructor;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.User;
import ru.otus.java.professional.yampolskiy.ttuserservice.exceptions.ValidationException;
import ru.otus.java.professional.yampolskiy.ttuserservice.repositories.UserRepository;

@RequiredArgsConstructor
public class CommonUserValidator  implements Validator<User>  {

    public void validate(User user) {
        if (user == null) {
            throw new ValidationException("User cannot be null");
        }

        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new ValidationException("Username cannot be null or empty");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new ValidationException("Password cannot be null or empty");
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ValidationException("Email cannot be null or empty");
        }
    }
}
