package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.services.OAuth2AuthorizationService;

@RestController
@RequestMapping("/api/v1/tokens")
@RequiredArgsConstructor
public class TokenController {

    private final OAuth2AuthorizationService authorizationService;

    @DeleteMapping("/user/{username}")
    public ResponseEntity<Void> revokeAllTokensForUser(@PathVariable String username) {
        authorizationService.revokeAllTokensForUser(username);
        return ResponseEntity.noContent().build();
    }
}
