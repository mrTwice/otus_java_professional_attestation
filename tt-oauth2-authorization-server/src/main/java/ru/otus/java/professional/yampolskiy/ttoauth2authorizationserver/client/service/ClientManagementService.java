package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.client.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.client.entity.RegisteredClientEntity;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.common.exception.EntityNotFoundException;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.client.repository.RegisteredClientJpaRepository;

import java.time.Instant;
import java.util.*;


@Service
@RequiredArgsConstructor
public class ClientManagementService {

    private final RegisteredClientJpaRepository clientRepository;

    @Transactional(readOnly = true)
    public List<RegisteredClientEntity> getAllClients() {
        return clientRepository.findAll();
    }

    @Transactional
    public RegisteredClientEntity saveClient(RegisteredClientEntity client) {
        if (client.getId() == null) {
            client.setId(UUID.randomUUID().toString()); // Генерируем уникальный ID
        }
        if (client.getClientIdIssuedAt() == null) {
            client.setClientIdIssuedAt(Instant.now()); // Устанавливаем дату выдачи clientId
        }
        return clientRepository.save(client);
    }

    @Transactional(readOnly = true)
    public RegisteredClientEntity getClientByClientId(String clientId) {
        return clientRepository.findByClientId(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with clientId: " + clientId));
    }

    @Transactional(readOnly = true)
    public boolean existsByClientId(String clientId) {
        return clientRepository.existsByClientId(clientId);
    }

    @Transactional
    public void deleteClientByClientId(String clientId) {
        RegisteredClientEntity client = getClientByClientId(clientId); // Проверяем существование клиента
        clientRepository.delete(client);
    }
}