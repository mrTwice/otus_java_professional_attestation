package ru.otus.java.professional.yampolskiy.ttuserservice.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.Role;
import ru.otus.java.professional.yampolskiy.ttuserservice.validators.Validator;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.User;
import ru.otus.java.professional.yampolskiy.ttuserservice.repositories.UserRepository;
import ru.otus.java.professional.yampolskiy.ttuserservice.repositories.RoleRepository;
import ru.otus.java.professional.yampolskiy.ttuserservice.validators.*;


@Configuration
public class ValidatorConfigurations {

    @Bean
    public Validator<Role> commonRoleValidator() {
        return new CommonRoleValidator();
    }

    @Bean
    public Validator<User> commonUserValidator(UserRepository userRepository) {
        return new CommonUserValidator(userRepository);
    }

    @Bean
    public Validator<Role> roleUniqueValidator(RoleRepository roleRepository) {
        return new RoleUniqueValidator(roleRepository);
    }

    @Bean
    public Validator<String> userEmailValidator() {
        return new UserEmailValidator();
    }

    @Bean
    public Validator<User> userUniqueValidator(UserRepository userRepository) {
        return new UserUniqueValidator(userRepository);
    }
}


