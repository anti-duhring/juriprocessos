package com.limatech.juriprocessos.services.interfaces;

import com.limatech.juriprocessos.exceptions.users.ForbiddenActionException;
import com.limatech.juriprocessos.models.users.entity.Role;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public interface UserValidation {

    void validateUserPermission(UUID id);

    default void validateUserAdminPermission() {
        boolean isUserAdmin =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(role -> role.toString().equals(Role.ADMIN.toString()));

        if(!isUserAdmin) {
            throw new ForbiddenActionException();
        }
    };
}
