package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.entities.OAuth2AuthorizationEntity;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.exceptions.EntityNotFoundException;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.repositories.OAuth2AuthorizationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OAuth2AuthorizationService {

    private final OAuth2AuthorizationRepository authorizationRepository;

    @Transactional
    public OAuth2AuthorizationEntity saveAuthorization(OAuth2AuthorizationEntity authorization) {
        return authorizationRepository.save(authorization);
    }

    @Transactional(readOnly = true)
    public OAuth2AuthorizationEntity getAuthorizationById(String id) {
        return authorizationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Authorization not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public OAuth2AuthorizationEntity getAuthorizationByAccessToken(String accessTokenValue) {
        return authorizationRepository.findByAccessTokenValue(accessTokenValue)
                .orElseThrow(() -> new EntityNotFoundException("Authorization not found for access token"));
    }

    @Transactional(readOnly = true)
    public OAuth2AuthorizationEntity getAuthorizationByRefreshToken(String refreshTokenValue) {
        return authorizationRepository.findByRefreshTokenValue(refreshTokenValue)
                .orElseThrow(() -> new EntityNotFoundException("Authorization not found for refresh token"));
    }

    @Transactional
    public void deleteAuthorizationByAccessToken(String accessTokenValue) {
        OAuth2AuthorizationEntity authorization = getAuthorizationByAccessToken(accessTokenValue);
        authorizationRepository.delete(authorization);
    }

    @Transactional
    public void deleteAuthorizationByRefreshToken(String refreshTokenValue) {
        OAuth2AuthorizationEntity authorization = getAuthorizationByRefreshToken(refreshTokenValue);
        authorizationRepository.delete(authorization);
    }

    @Transactional(readOnly = true)
    public List<OAuth2AuthorizationEntity> getAuthorizationsByClientId(String registeredClientId) {
        return authorizationRepository.findByRegisteredClientId(registeredClientId);
    }

    @Transactional
    public void revokeAllTokensForUser(String username) {
        List<OAuth2AuthorizationEntity> authorizations = authorizationRepository.findByPrincipalName(username);
        authorizationRepository.deleteAll(authorizations);
    }
}