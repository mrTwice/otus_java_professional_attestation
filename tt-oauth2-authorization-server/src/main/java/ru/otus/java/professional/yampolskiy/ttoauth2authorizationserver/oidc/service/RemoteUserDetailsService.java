package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.oidc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.external.exceptions.IntegrationException;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.external.users.client.UserClient;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.external.users.dto.UserPrincipalDTO;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class RemoteUserDetailsService implements UserDetailsService {

    private final UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserPrincipalDTO user = userClient.findByUsername(username);
            log.debug("👤 DTO от user-service: {}", user.toString());
            log.debug("👤 Получен пользователь: {}, active={}", user.getUsername(), user.isActive());
            return User.withUsername(user.getUsername())
                    .password(user.getPassword())
                    .authorities(user.getPermissions().toArray(new String[0]))
                    .accountLocked(user.isLocked())
                    .disabled(!user.isActive())
                    .accountExpired(false)
                    .credentialsExpired(false)
                    .build();

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

    private boolean isAccountExpired(Instant expiresAt) {
        return expiresAt != null && Instant.now().isAfter(expiresAt);
    }

    private boolean isCredentialsExpired(Instant expiresAt) {
        return expiresAt != null && Instant.now().isAfter(expiresAt);
    }
}