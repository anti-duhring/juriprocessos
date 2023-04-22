package com.limatech.juriprocessos.dtos.process;

import com.limatech.juriprocessos.exceptions.process.ProcessNotFoundException;

import java.util.UUID;

public class ManageProcessToGroupDTO {

    private UUID id;

    public UUID getId() {
        return id;
    }

    public ManageProcessToGroupDTO(UUID id) {
        validateProcessId(id);

        this.id = id;
    }

    private void validateProcessId(UUID id) {
        if(id == null) {
            throw new ProcessNotFoundException("Process ID cannot be null");
        }
    }
}
