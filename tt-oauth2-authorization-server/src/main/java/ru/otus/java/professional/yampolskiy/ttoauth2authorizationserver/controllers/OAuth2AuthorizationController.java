package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.dtos.OAuth2TokenIntrospectionResponse;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.entities.OAuth2AuthorizationEntity;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.services.OAuth2AuthorizationServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authorizations")
@RequiredArgsConstructor
public class OAuth2AuthorizationController {

    private final OAuth2AuthorizationServiceImpl authorizationService;

    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OAuth2AuthorizationEntity>> getAuthorizationsByClientId(@PathVariable String clientId) {
        return ResponseEntity.ok(authorizationService.getAuthorizationsByClientId(clientId));
    }

    @DeleteMapping("/user/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> revokeAllTokensForUser(@PathVariable String username) {
        authorizationService.revokeAllTokensForUser(username);
        return ResponseEntity.noContent().build();
    }
}
