package ru.otus.java.professional.yampolskiy.tttaskservice.comments.security;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component("commentAccessPolicy")
public class CommentAccessPolicy {

    public boolean hasPermission(Authentication authentication, String permission) {
        Jwt jwt = extractJwt(authentication);
        if (jwt == null) return false;

        List<String> permissions = jwt.getClaimAsStringList("permissions");
        return permissions != null && permissions.contains(permission);
    }

    public boolean isSelf(Authentication authentication, UUID userId) {
        UUID currentUserId = getUserId(authentication);
        return currentUserId != null && currentUserId.equals(userId);
    }

    public UUID getUserId(Authentication authentication) {
        Jwt jwt = extractJwt(authentication);
        if (jwt == null) return null;

        try {
            return UUID.fromString(jwt.getSubject());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }


    public boolean canCreateComment(Authentication authentication) {
        return hasPermission(authentication, "comment:create");
    }

    public boolean canViewComment(Authentication authentication) {
        return hasPermission(authentication, "comment:view");
    }

    public boolean canUpdateComment(Authentication authentication) {
        return hasPermission(authentication, "comment:update");
    }

    public boolean canDeleteComment(Authentication authentication) {
        return hasPermission(authentication, "comment:delete");
    }

    public boolean canManageCommentOfUser(Authentication authentication, UUID authorId) {

        return hasPermission(authentication, "comment:update")
                || hasPermission(authentication, "comment:delete")
                || isSelf(authentication, authorId);
    }

    public boolean canEditOwnCommentOnly(Authentication authentication, UUID authorId) {
        return isSelf(authentication, authorId);
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
