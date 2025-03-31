package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.oidc.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.integrations.users.dto.UserCreateDTO;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.oidc.dto.UserRegistrationRequest;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.oidc.mapper.UserRegistrationMapper;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.oidc.service.UserRegistrationService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class RegistrationController {

    private final UserRegistrationService userRegistrationService;
    private final UserRegistrationMapper userRegistrationMapper;

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        UserCreateDTO userCreateDTO = userRegistrationMapper.toUserCreateDTO(request);
        userRegistrationService.registerLocalUser(userCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}