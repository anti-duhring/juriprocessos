package com.limatech.juriprocessos.dtos.process;

import com.limatech.juriprocessos.models.process.entity.Activity;
import com.limatech.juriprocessos.models.process.entity.Process;

import java.time.LocalDateTime;
import java.util.UUID;

public class CreateActivityDTO {

    private Process process;

    private UUID processId;

    private String name;

    private String description;

    private String type;

    private LocalDateTime date;

    public CreateActivityDTO(UUID processId, String name, String description, String type, LocalDateTime date) {
        this.processId = processId;
        this.name = name;
        this.description = description;
        this.type = type;
        this.date = date;
    }

    public CreateActivityDTO() {

    }

    public Activity toEntity() {
        return new Activity(process, name, description, type, date.toString());
    }

    public UUID getProcessId() {
        return processId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setProcess(Process process) {
        this.process = process;
    }
}
