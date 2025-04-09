package ru.otus.java.professional.yampolskiy.tttaskservice.tasks.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import ru.otus.java.professional.yampolskiy.tttaskservice.common.securiry.AbstractAccessPolicy;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.exceptions.task.TaskAccessDeniedException;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component("taskAccessPolicy")
public class TaskAccessPolicy extends AbstractAccessPolicy {

    public boolean canCreateTask(Authentication auth) {
        return hasPermission(auth, "task:create");
    }

    public void checkCreateTask(Authentication auth) {
        if (!canCreateTask(auth)) {
            throw new TaskAccessDeniedException();
        }
    }

    public boolean canViewTask(Authentication auth) {
        return hasPermission(auth, "task:view");
    }

    public void checkViewTask(Authentication auth) {
        if (!canViewTask(auth)) {
            throw new TaskAccessDeniedException();
        }
    }

    public boolean canUpdateTask(Authentication auth) {
        return hasPermission(auth, "task:update");
    }

    public void checkUpdateTask(Authentication auth) {
        if (!canUpdateTask(auth)) {
            throw new TaskAccessDeniedException();
        }
    }

    public boolean canDeleteTask(Authentication auth) {
        return hasPermission(auth, "task:delete");
    }

    public void checkDeleteTask(Authentication auth) {
        if (!canDeleteTask(auth)) {
            throw new TaskAccessDeniedException();
        }
    }

    public boolean canAssignTask(Authentication auth) {
        return hasPermission(auth, "task:assign");
    }

    public void checkAssignTask(Authentication auth) {
        if (!canAssignTask(auth)) {
            throw new TaskAccessDeniedException();
        }
    }
}


