package ru.otus.java.professional.yampolskiy.tttaskservice.taskstatuses.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component("taskStatusAccessPolicy")
public class TaskStatusAccessPolicy {

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

    public boolean canViewTaskStatus(Authentication auth) {
        return hasPermission(auth, "task-status:view");
    }

    public boolean canCreateTaskStatus(Authentication auth) {
        return hasPermission(auth, "task-status:create");
    }

    public boolean canUpdateTaskStatus(Authentication auth) {
        return hasPermission(auth, "task-status:update");
    }

    public boolean canDeleteTaskStatus(Authentication auth) {
        return hasPermission(auth, "task-status:delete");
    }

}
