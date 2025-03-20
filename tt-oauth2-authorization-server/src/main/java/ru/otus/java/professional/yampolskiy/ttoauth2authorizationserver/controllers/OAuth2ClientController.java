package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.entities.OAuth2ClientEntity;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.services.OAuth2ClientService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class OAuth2ClientController {

    private final OAuth2ClientService clientService;

    @GetMapping
    public ResponseEntity<List<OAuth2ClientEntity>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<OAuth2ClientEntity> getClientByClientId(@PathVariable String clientId) {
        return ResponseEntity.ok(clientService.getClientByClientId(clientId));
    }

    @GetMapping("/exists/{clientId}")
    public ResponseEntity<Boolean> checkIfClientExists(@PathVariable String clientId) {
        return ResponseEntity.ok(clientService.existsByClientId(clientId));
    }

    @PostMapping
    public ResponseEntity<OAuth2ClientEntity> createClient(@RequestBody OAuth2ClientEntity client) {
        return ResponseEntity.ok(clientService.saveClient(client));
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> deleteClient(@PathVariable String clientId) {
        clientService.deleteClientByClientId(clientId);
        return ResponseEntity.noContent().build();
    }
}