package com.limatech.juriprocessos.services.interfaces;

import com.limatech.juriprocessos.exceptions.users.ForbiddenActionException;
import com.limatech.juriprocessos.models.process.entity.Group;
import com.limatech.juriprocessos.models.process.entity.Process;
import com.limatech.juriprocessos.models.users.entity.Role;
import com.limatech.juriprocessos.models.users.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.UUID;

public interface UserValidation {

    void validateUserPermission(UUID id);

    default boolean isUserAdmin(User user) {
        return user.getAuthorities().stream().anyMatch(role -> role.toString().equals(Role.ADMIN.toString()));
    }

    default boolean userCanWriteOnProcess(User user, Process process) {
        List<Group> groupsThatUserCanWrite = user.getCanWrite();
        List<UUID> groupsThatUserCanWriteIds = groupsThatUserCanWrite.stream().map(Group::getId).toList();

        List<Group> groupsFromProcess = process.getGroups();
        List<UUID> groupsFromProcessIds = groupsFromProcess.stream().map(Group::getId).toList();

        if(process.getUser().getId().toString().equals(user.getId().toString())) {
            return true;
        }

        for (UUID uuid : groupsThatUserCanWriteIds) {
            if (groupsFromProcessIds.contains(uuid)) {
                return true;
            }
        }

        return false;
    }

    default boolean userCanWriteOnGroup(User user, Group group) {
        List<Group> groupsThatUserCanWrite = user.getCanWrite();
        List<UUID> groupsThatUserCanWriteIds = groupsThatUserCanWrite.stream().map(Group::getId).toList();

        for (UUID uuid : groupsThatUserCanWriteIds) {
            if (uuid.toString().equals(group.getId().toString())) {
                return true;
            }
        }

        return false;
    }

}
