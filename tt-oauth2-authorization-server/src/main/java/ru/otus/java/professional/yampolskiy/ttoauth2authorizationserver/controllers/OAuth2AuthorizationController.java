package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.entities.OAuth2AuthorizationEntity;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.services.OAuth2AuthorizationService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authorizations")
@RequiredArgsConstructor
public class OAuth2AuthorizationController {

    private final OAuth2AuthorizationService authorizationService;

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<OAuth2AuthorizationEntity>> getAuthorizationsByClientId(@PathVariable String clientId) {
        return ResponseEntity.ok(authorizationService.getAuthorizationsByClientId(clientId));
    }

    @GetMapping("/access/{token}")
    public ResponseEntity<OAuth2AuthorizationEntity> getAuthorizationByAccessToken(@PathVariable String token) {
        return ResponseEntity.ok(authorizationService.getAuthorizationByAccessToken(token));
    }

    @GetMapping("/refresh/{token}")
    public ResponseEntity<OAuth2AuthorizationEntity> getAuthorizationByRefreshToken(@PathVariable String token) {
        return ResponseEntity.ok(authorizationService.getAuthorizationByRefreshToken(token));
    }

    @DeleteMapping("/access/{token}")
    public ResponseEntity<Void> revokeAuthorizationByAccessToken(@PathVariable String token) {
        authorizationService.deleteAuthorizationByAccessToken(token);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/refresh/{token}")
    public ResponseEntity<Void> revokeAuthorizationByRefreshToken(@PathVariable String token) {
        authorizationService.deleteAuthorizationByRefreshToken(token);
        return ResponseEntity.noContent().build();
    }
}
