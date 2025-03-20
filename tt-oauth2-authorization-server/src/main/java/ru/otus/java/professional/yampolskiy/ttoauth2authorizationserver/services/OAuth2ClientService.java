package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.entities.OAuth2ClientEntity;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.exceptions.EntityNotFoundException;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.repositories.OAuth2ClientRepository;

import java.time.Instant;
import java.util.*;


@Service
@RequiredArgsConstructor
public class OAuth2ClientService {

    private final OAuth2ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public List<OAuth2ClientEntity> getAllClients() {
        return clientRepository.findAll();
    }

    @Transactional
    public OAuth2ClientEntity saveClient(OAuth2ClientEntity client) {
        if (client.getId() == null) {
            client.setId(UUID.randomUUID().toString()); // Генерируем уникальный ID
        }
        if (client.getClientIdIssuedAt() == null) {
            client.setClientIdIssuedAt(Instant.now()); // Устанавливаем дату выдачи clientId
        }
        return clientRepository.save(client);
    }

    @Transactional(readOnly = true)
    public OAuth2ClientEntity getClientByClientId(String clientId) {
        return clientRepository.findByClientId(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with clientId: " + clientId));
    }

    @Transactional(readOnly = true)
    public boolean existsByClientId(String clientId) {
        return clientRepository.existsByClientId(clientId);
    }

    @Transactional
    public void deleteClientByClientId(String clientId) {
        OAuth2ClientEntity client = getClientByClientId(clientId); // Проверяем существование клиента
        clientRepository.delete(client);
    }
}