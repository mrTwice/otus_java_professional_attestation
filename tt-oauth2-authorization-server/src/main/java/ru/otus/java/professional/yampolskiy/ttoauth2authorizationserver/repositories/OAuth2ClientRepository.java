package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.entities.OAuth2ClientEntity;

import java.util.Optional;

@Repository
public interface OAuth2ClientRepository extends JpaRepository<OAuth2ClientEntity, String> {

    @Transactional(readOnly = true)
    Optional<OAuth2ClientEntity> findByClientId(String clientId);

    @Transactional(readOnly = true)
    boolean existsByClientId(String clientId);

    @Modifying
    @Transactional
    void deleteByClientId(String clientId);
}
