package ru.otus.java.professional.yampolskiy.ttuserservice.initializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.Role;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.User;
import ru.otus.java.professional.yampolskiy.ttuserservice.services.RoleService;
import ru.otus.java.professional.yampolskiy.ttuserservice.services.UserService;

import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@Order(2)
@RequiredArgsConstructor
public class TestUserInitializer implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;

    @Override
    public void run(String... args) {
        log.info("🧪 Initializing test user...");

        String username = "testuser";
        String email = "testuser@example.com";

        if (userService.existsByUsername(username)) {
            log.info("ℹ️ Test user '{}' already exists", username);
            return;
        }

        Role adminRole = roleService.getRoleByName("ADMIN");
        String passwordHash = "$2a$10$7JPuN6sXZ86W4wL81AkvCu93WAKT2mAzgDLECh2Xtc5TdlX0Qyjsy";  // password

        User user = User.builder()
                .username(username)
                .password(passwordHash)
                .email(email)
                .emailVerified(true)
                .firstName("Test")
                .lastName("User")
                .locale("en")
                .pictureUrl("https://example.com/avatar.png")
                .isActive(true)
                .oidcProvider("local")
                .oidcSubject(UUID.randomUUID().toString())
                .roles(Set.of(adminRole))
                .build();

        userService.createUser(user);

        log.info("✅ Test user '{}' created successfully", username);
    }
}
