package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.oidc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.oidc.repository.InMemoryUserInfoRepository;

@Service
@RequiredArgsConstructor
public class OidcUserInfoService {

    private final InMemoryUserInfoRepository inMemoryUserInfoRepository;

    public OidcUserInfo loadUser(String username) {
        return new OidcUserInfo(inMemoryUserInfoRepository.findByUsername(username));
    }


}