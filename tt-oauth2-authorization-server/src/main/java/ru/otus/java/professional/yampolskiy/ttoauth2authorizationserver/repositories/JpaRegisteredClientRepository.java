package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.services.OAuth2ClientMapper;

@Repository
@RequiredArgsConstructor
public class JpaRegisteredClientRepository implements RegisteredClientRepository {

    private final OAuth2ClientRepository clientRepository;
    private final OAuth2ClientMapper clientMapper;

    @Override
    @Transactional
    public void save(RegisteredClient registeredClient) {
        clientRepository.save(clientMapper.from(registeredClient));
    }

    @Override
    @Transactional(readOnly = true)
    public RegisteredClient findById(String id) {
        return clientRepository.findById(id)
                .map(clientMapper::toRegisteredClient)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public RegisteredClient findByClientId(String clientId) {
        return clientRepository.findByClientId(clientId)
                .map(clientMapper::toRegisteredClient)
                .orElse(null);
    }
}
