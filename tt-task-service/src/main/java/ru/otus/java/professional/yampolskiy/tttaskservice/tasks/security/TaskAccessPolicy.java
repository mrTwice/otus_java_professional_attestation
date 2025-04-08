package ru.otus.java.professional.yampolskiy.tttaskservice.tasks.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component("taskAccessPolicy")
public class TaskAccessPolicy {

    public boolean hasPermission(Authentication authentication, String permission) {
        Jwt jwt = extractJwt(authentication);
        if (jwt == null) return false;

        List<String> permissions = getClaimAsList(jwt, "permissions");
        List<String> scopes = jwt.getClaimAsStringList("scope");

        log.debug("🔍 Checking permission [{}], permissions: {}, scopes: {}", permission, permissions, scopes);

        return permissions.contains(permission) || (scopes != null && scopes.contains(permission));
    }

    public boolean hasAnyPermission(Authentication authentication, String... permissionsToCheck) {
        Jwt jwt = extractJwt(authentication);
        if (jwt == null) return false;

        List<String> permissions = getClaimAsList(jwt, "permissions");
        List<String> scopes = jwt.getClaimAsStringList("scope");

        return Arrays.stream(permissionsToCheck)
                .anyMatch(p -> permissions.contains(p) || (scopes != null && scopes.contains(p)));
    }

    public boolean canCreateTask(Authentication auth) {
        return hasPermission(auth, "task:create");
    }

    public boolean canViewTask(Authentication auth) {
        return hasPermission(auth, "task:view");
    }

    public boolean canUpdateTask(Authentication auth) {
        return hasPermission(auth, "task:update");
    }

    public boolean canDeleteTask(Authentication auth) {
        return hasPermission(auth, "task:delete");
    }

    public boolean canAssignTask(Authentication auth) {
        return hasPermission(auth, "task:assign");
    }

    private Jwt extractJwt(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt jwt) {
            return jwt;
        }

        return null;
    }

    private List<String> getClaimAsList(Jwt jwt, String claimName) {
        Object claim = jwt.getClaims().get(claimName);
        if (claim instanceof Collection<?> collection) {
            return collection.stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .toList();
        }
        return List.of();
    }
}

