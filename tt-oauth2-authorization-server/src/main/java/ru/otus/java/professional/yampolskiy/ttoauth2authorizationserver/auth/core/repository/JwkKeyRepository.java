package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.auth.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.auth.core.entity.JwkKey;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JwkKeyRepository extends JpaRepository<JwkKey, UUID> {
    Optional<JwkKey> findFirstByIsActiveTrueOrderByCreatedAtDesc();

    List<JwkKey> findAllByIsActiveTrueOrderByCreatedAtDesc();

    @Modifying
    @Transactional
    @Query("UPDATE JwkKey k SET k.isActive = false WHERE k.isActive = true")
    void updateAllSetInactive();
}