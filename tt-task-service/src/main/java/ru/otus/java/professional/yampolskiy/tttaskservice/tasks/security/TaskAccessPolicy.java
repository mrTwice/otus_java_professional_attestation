package ru.otus.java.professional.yampolskiy.tttaskservice.tasks.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component("taskAccessPolicy")
public class TaskAccessPolicy {

    public boolean hasPermission(Authentication authentication, String permission) {
        Jwt jwt = extractJwt(authentication);
        if (jwt == null) return false;

        List<String> permissions = jwt.getClaimAsStringList("permissions");
        return permissions != null && permissions.contains(permission);
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

}
