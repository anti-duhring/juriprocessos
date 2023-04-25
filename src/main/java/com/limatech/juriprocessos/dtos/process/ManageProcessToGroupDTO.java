package com.limatech.juriprocessos.dtos.process;

import com.limatech.juriprocessos.exceptions.process.ProcessNotFoundException;

import java.util.UUID;

public class ManageProcessToGroupDTO {

    private UUID processId;

    public ManageProcessToGroupDTO(UUID processId) {
        validateProcessId(processId);

        this.processId = processId;
    }

    public UUID getProcessId() {
        return processId;
    }

    public void setProcessId(UUID processId) {
        this.processId = processId;
    }

    private void validateProcessId(UUID id) {
        if(id == null) {
            throw new ProcessNotFoundException("Process ID cannot be null");
        }
    }
}
