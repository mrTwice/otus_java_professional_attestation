package ru.otus.java.professional.yampolskiy.ttuserservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.user.UserCreateDTO;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.User;
import ru.otus.java.professional.yampolskiy.ttuserservice.mappers.UserMapper;
import ru.otus.java.professional.yampolskiy.ttuserservice.services.UserService;

@Slf4j
@RestController
@RequestMapping("/api/v1/internal/users")
@RequiredArgsConstructor
public class InternalUserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    @PreAuthorize("@accessPolicy.isInternalClient(authentication)")
    public ResponseEntity<Void> createFromSas(@RequestBody @Valid UserCreateDTO dto,
                                              Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticatedPrincipal principal) {
            log.info("🐾 Principal claims: {}", principal.getAttributes());
        }
        User user = userMapper.toEntityFromUserCreateDTO(dto);
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
