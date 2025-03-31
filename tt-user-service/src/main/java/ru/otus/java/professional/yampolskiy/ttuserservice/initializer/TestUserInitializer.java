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

import java.net.MalformedURLException;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
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
    public void run(String... args) throws MalformedURLException {
        log.info("🧪 Initializing test user...");

        String username = "test-user";
        String email = "testuser@example.com";

        if (userService.existsByUsername(username)) {
            log.info("ℹ️ Test user '{}' already exists", username);
            return;
        }

        Role adminRole = roleService.getRoleByName("ADMIN");
        String passwordHash = "{bcrypt}$2y$10$NyUZxVGTHlyTvEewegg21eGWU2Ayek67nWjEeSOGTni8VDCoxB4Em";  // password

        Map<String, Object> address = Map.of(
                "formatted", "123 Main St\nSpringfield, USA",
                "street_address", "123 Main St",
                "locality", "Springfield",
                "region", "IL",
                "postal_code", "62704",
                "country", "USA"
        );

        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .username(username)
                .password(passwordHash)
                .email(email)
                .emailVerified(true)
                .firstName("Test")
                .lastName("User")
                .middleName("Middle")
                .nickname("Tester")
                .profile(URI.create("https://example.com/profile/test-user").toURL())
                .website(URI.create("https://example.com").toURL())
                .pictureUrl(URI.create("https://example.com/avatar.png").toURL())
                .gender("other")
                .birthdate(LocalDate.of(1990, 1, 1))
                .zoneinfo("Europe/Moscow")
                .locale("en")
                .phoneNumber("+1-555-123-4567")
                .phoneNumberVerified(true)
                .updatedAtOidc(Instant.now())
                .address(address)
                .active(true)
                .locked(false)
                .oidcProvider("local")
                .oidcSubject(userId.toString())
                .roles(Set.of(adminRole))
                .build();

        userService.createUser(user);

        log.info("✅ Test user '{}' created successfully", username);
    }
}

