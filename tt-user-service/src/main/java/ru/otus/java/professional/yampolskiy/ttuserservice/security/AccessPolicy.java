package ru.otus.java.professional.yampolskiy.ttuserservice.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component("accessPolicy")
public class AccessPolicy {

    public boolean hasPermission(Authentication authentication, String permission) {
        Jwt jwt = extractJwt(authentication);
        if (jwt == null) return false;

        List<String> permissions = jwt.getClaimAsStringList("permissions");
        return permissions != null && permissions.contains(permission);
    }

    public boolean hasAnyPermission(Authentication authentication, String... permissionsToCheck) {
        Jwt jwt = extractJwt(authentication);
        if (jwt == null) return false;

        List<String> permissions = jwt.getClaimAsStringList("permissions");
        if (permissions == null) return false;

        return Arrays.stream(permissionsToCheck).anyMatch(permissions::contains);
    }

    public boolean hasRole(Authentication authentication, String role) {
        Jwt jwt = extractJwt(authentication);
        if (jwt == null) return false;

        List<String> roles = jwt.getClaimAsStringList("roles");
        return roles != null && roles.contains(role);
    }

    public boolean hasAnyRole(Authentication authentication, String... rolesToCheck) {
        Jwt jwt = extractJwt(authentication);
        if (jwt == null) return false;

        List<String> roles = jwt.getClaimAsStringList("roles");
        if (roles == null) return false;

        return Arrays.stream(rolesToCheck).anyMatch(roles::contains);
    }

    public boolean hasPermissionOrRole(Authentication authentication, String permission, String role) {
        return hasPermission(authentication, permission) || hasRole(authentication, role);
    }

    public boolean isSelf(Authentication authentication, UUID userId) {
        Jwt jwt = extractJwt(authentication);
        if (jwt == null) return false;

        String subject = jwt.getSubject();
        if (subject == null) return false;

        try {
            UUID principalId = UUID.fromString(subject);
            return principalId.equals(userId);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean canAccessUser(Authentication authentication, UUID targetUserId) {
        return isSelf(authentication, targetUserId) ||
                hasPermissionOrRole(authentication, "user:view", "ADMIN");
    }

    public boolean canUpdateUser(Authentication authentication, UUID targetUserId) {
        return isSelf(authentication, targetUserId) ||
                hasPermissionOrRole(authentication, "user:update", "ADMIN");
    }

    public boolean canChangePassword(Authentication authentication, UUID userId) {
        return isSelf(authentication, userId);
    }

    public boolean canAssignRoles(Authentication authentication) {
        return hasPermissionOrRole(authentication, "user:assign-roles", "ADMIN");
    }

    public boolean isAdmin(Authentication authentication) {
        return hasRole(authentication, "ADMIN");
    }

    public boolean canCreateUsers(Authentication authentication) {
        return hasPermissionOrRole(authentication, "user:manage", "ADMIN");
    }

    public boolean canViewUsers(Authentication authentication) {
        return hasPermissionOrRole(authentication, "user:view", "ADMIN")
                || hasScope(authentication, "user:view");
    }

    public boolean canUpdateUsers(Authentication authentication) {
        return hasPermissionOrRole(authentication, "user:update", "ADMIN");
    }

    public boolean canDeleteUsers(Authentication authentication) {
        return hasPermissionOrRole(authentication, "user:delete", "ADMIN");
    }


    public boolean canAdminUpdateUsers(Authentication authentication) {
        return hasPermissionOrRole(authentication, "user:manage", "ADMIN");
    }

    public boolean canViewRoles(Authentication authentication) {
        return hasPermissionOrRole(authentication, "role:view", "ADMIN");
    }

    public boolean canManagePermissions(Authentication authentication) {
        return hasPermissionOrRole(authentication, "permission:create", "ADMIN") ||
                hasPermissionOrRole(authentication, "permission:update", "ADMIN") ||
                hasPermissionOrRole(authentication, "permission:delete", "ADMIN");
    }

    public boolean canChangePasswordFor(Authentication authentication, UUID targetUserId) {
        return isSelf(authentication, targetUserId) || hasRole(authentication, "ADMIN");
    }

    public boolean hasScope(Authentication authentication, String scope) {
        Jwt jwt = extractJwt(authentication);
        if (jwt == null) return false;

        List<String> scopes = jwt.getClaimAsStringList("scope");
        return scopes != null && scopes.contains(scope);
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

