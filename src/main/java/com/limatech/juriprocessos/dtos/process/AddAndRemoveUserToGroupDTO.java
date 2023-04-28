package com.limatech.juriprocessos.dtos.process;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddAndRemoveUserToGroupDTO {

    private UUID groupId;

    private List<UUID> canRead;

    private List<UUID> canWrite;

    public AddAndRemoveUserToGroupDTO(UUID groupId, List<UUID> canRead, List<UUID> canWrite) {
        this.groupId = groupId;
        this.canRead = canRead;
        this.canWrite = canWrite;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public List<UUID> getCanRead() {
        return canRead;
    }

    public List<UUID> getCanWrite() {
        return canWrite;
    }
}
