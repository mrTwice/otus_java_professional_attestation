package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.oidc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.integrations.exceptions.IntegrationException;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.integrations.users.client.UserProfileClient;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.integrations.users.dto.UserPrincipalDTO;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class RemoteUserDetailsService implements UserDetailsService {

    //TODO подумать о переносе сервиса в external/users
    private final UserProfileClient userProfileClient;
    private final UserAuthCache userAuthCache;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("🔐 RemoteUserDetailsService.loadUserByUsername called for: {}", username);

        try {
            UserPrincipalDTO user = userProfileClient.findByUsername(username);
            userAuthCache.put(username, user);
            log.debug("👤 DTO от user-service: {}", user);
            return user;

        } catch (IntegrationException e) {
            if ("USER_NOT_FOUND".equals(e.getCode())) {
                log.warn("🔒 Пользователь не найден в User-Service: {}", username);
                throw new UsernameNotFoundException("User not found: " + username);
            }

            log.error("💥 Ошибка User-Service при загрузке пользователя: {}", username, e);
            throw new InternalAuthenticationServiceException("UserService unavailable", e);

        } catch (Exception e) {
            log.error("🔥 Непредвиденная ошибка при загрузке пользователя: {}", username, e);
            throw new InternalAuthenticationServiceException("Unexpected error", e);
        }
    }
}