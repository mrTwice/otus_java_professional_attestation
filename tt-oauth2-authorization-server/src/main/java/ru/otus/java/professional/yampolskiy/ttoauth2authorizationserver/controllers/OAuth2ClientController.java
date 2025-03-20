package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.entities.OAuth2ClientEntity;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.services.OAuth2ClientServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class OAuth2ClientController {

    private final OAuth2ClientServiceImpl clientService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OAuth2ClientEntity>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/{clientId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OAuth2ClientEntity> getClientByClientId(@PathVariable String clientId) {
        return ResponseEntity.ok(clientService.getClientByClientId(clientId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OAuth2ClientEntity> createClient(@RequestBody OAuth2ClientEntity client) {
        return ResponseEntity.ok(clientService.saveClient(client));
    }

    @DeleteMapping("/{clientId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteClient(@PathVariable String clientId) {
        clientService.deleteClientByClientId(clientId);
        return ResponseEntity.noContent().build();
    }
}