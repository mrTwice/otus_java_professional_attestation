package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.repositories;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.services.OAuth2ClientMapper;

@Repository
@RequiredArgsConstructor
public class JpaRegisteredClientRepository implements RegisteredClientRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(JpaRegisteredClientRepository.class);
    private final OAuth2ClientRepository clientRepository;
    private final OAuth2ClientMapper clientMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void save(RegisteredClient registeredClient) {
        try {
        clientRepository.save(clientMapper.from(registeredClient));
    } catch (Exception e) {
        LOGGER.error("Error while saving registeredClient: {}", registeredClient, e);
        throw e;
    }
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
        try {
            return clientRepository.findByClientId(clientId)
                    .map(clientMapper::toRegisteredClient)
                    .orElse(null);
        } catch (Exception e) {
            LOGGER.error("Error while finding client by clientId: {}", clientId, e);
            throw e;
        }
    }

    public void deleteById(String id) {
        clientRepository.deleteById(id);
    }
}
