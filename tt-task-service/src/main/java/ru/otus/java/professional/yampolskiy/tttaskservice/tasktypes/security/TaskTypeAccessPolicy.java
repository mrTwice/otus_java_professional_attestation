package ru.otus.java.professional.yampolskiy.tttaskservice.tasktypes.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component("taskTypeAccessPolicy")
public class TaskTypeAccessPolicy {

    public boolean hasPermission(Authentication authentication, String permission) {
        Jwt jwt = extractJwt(authentication);
        if (jwt == null) return false;

        List<String> permissions = jwt.getClaimAsStringList("permissions");
        return permissions != null && permissions.contains(permission);
    }

    public boolean canViewTaskType(Authentication authentication) {
        return hasPermission(authentication, "task-type:view");
    }

    public boolean canCreateTaskType(Authentication authentication) {
        return hasPermission(authentication, "task-type:create");
    }

    public boolean canUpdateTaskType(Authentication authentication) {
        return hasPermission(authentication, "task-type:update");
    }

    public boolean canDeleteTaskType(Authentication authentication) {
        return hasPermission(authentication, "task-type:delete");
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
}
