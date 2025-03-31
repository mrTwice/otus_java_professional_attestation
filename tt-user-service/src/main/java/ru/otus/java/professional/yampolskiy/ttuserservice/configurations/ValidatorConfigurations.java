package ru.otus.java.professional.yampolskiy.ttuserservice.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.Permission;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.Role;
import ru.otus.java.professional.yampolskiy.ttuserservice.repositories.PermissionRepository;
import ru.otus.java.professional.yampolskiy.ttuserservice.validators.Validator;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.User;
import ru.otus.java.professional.yampolskiy.ttuserservice.repositories.UserRepository;
import ru.otus.java.professional.yampolskiy.ttuserservice.repositories.RoleRepository;
import ru.otus.java.professional.yampolskiy.ttuserservice.validators.*;

import java.util.Set;


@Configuration
public class ValidatorConfigurations {

    @Bean
    public Validator<Role> commonRoleValidator() {
        return new CommonRoleValidator();
    }

    @Bean
    public Validator<User> commonUserValidator() {
        return new CommonUserValidator();
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

    @Bean
    public Validator<Permission> permissionValidator(){
        return new PermissionValidator();
    }

    @Bean
    public Validator<Permission> permissionUniqueValidator(){
        return new PermissionValidator();
    }

}


