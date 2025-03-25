package ru.otus.java.professional.yampolskiy.ttuserservice.initializer;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.Role;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.User;
import ru.otus.java.professional.yampolskiy.ttuserservice.repositories.RoleRepository;
import ru.otus.java.professional.yampolskiy.ttuserservice.repositories.UserRepository;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Transactional
public class AdminUserInitializer implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminUserInitializer.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("admin")) {

            LOGGER.info("Администратор создан");
        } else {
            LOGGER.info("Администратор уже существует");
        }
    }
}
