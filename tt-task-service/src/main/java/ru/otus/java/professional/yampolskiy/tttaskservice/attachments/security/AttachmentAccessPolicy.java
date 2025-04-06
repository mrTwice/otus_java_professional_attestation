package ru.otus.java.professional.yampolskiy.tttaskservice.attachments.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component("attachmentAccessPolicy")
public class AttachmentAccessPolicy {

    public boolean hasPermission(Authentication authentication, String permission) {
        Jwt jwt = extractJwt(authentication);
        if (jwt == null) return false;

        List<String> permissions = jwt.getClaimAsStringList("permissions");
        return permissions != null && permissions.contains(permission);
    }

    public boolean isSelf(Authentication authentication, UUID userId) {
        UUID subject = getUserId(authentication);
        return subject != null && subject.equals(userId);
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

    public boolean canCreateAttachment(Authentication authentication) {
        return hasPermission(authentication, "attachment:create");
    }

    public boolean canViewAttachment(Authentication authentication) {
        return hasPermission(authentication, "attachment:view");
    }

    public boolean canUpdateAttachment(Authentication authentication) {
        return hasPermission(authentication, "attachment:update");
    }

    public boolean canDeleteAttachment(Authentication authentication) {
        return hasPermission(authentication, "attachment:delete");
    }

    public boolean canViewAttachmentsOfUser(Authentication authentication, UUID userId) {
        return isSelf(authentication, userId) || canViewAttachment(authentication);
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
