package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.entities.OAuth2AuthorizationEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface OAuth2AuthorizationRepository extends JpaRepository<OAuth2AuthorizationEntity, String> {

    @Transactional(readOnly = true)
    Optional<OAuth2AuthorizationEntity> findByAccessTokenValue(String accessTokenValue);

    @Transactional(readOnly = true)
    Optional<OAuth2AuthorizationEntity> findByRefreshTokenValue(String refreshTokenValue);

    @Transactional(readOnly = true)
    Optional<OAuth2AuthorizationEntity> findByAuthorizationCodeValue(String authorizationCodeValue);

    @Transactional(readOnly = true)
    List<OAuth2AuthorizationEntity> findByRegisteredClientId(String registeredClientId);

    @Modifying
    @Transactional
    void deleteByAccessTokenValue(String accessTokenValue);

    @Modifying
    @Transactional
    void deleteByRefreshTokenValue(String refreshTokenValue);

    @Transactional(readOnly = true)
    List<OAuth2AuthorizationEntity> findByPrincipalName(String principal);

    @Query("""
                SELECT a FROM OAuth2AuthorizationEntity a 
                WHERE cast(a.accessTokenValue as string) = :token 
                   OR cast(a.refreshTokenValue as string) = :token 
                   OR cast(a.authorizationCodeValue as string) = :token
            """)
    Optional<OAuth2AuthorizationEntity> findByTokenValue(@Param("token") String token);
}