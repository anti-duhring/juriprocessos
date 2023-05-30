package com.limatech.juriprocessos.dtos.users;

import com.limatech.juriprocessos.models.process.entity.Process;
import com.limatech.juriprocessos.models.process.property.Description;
import com.limatech.juriprocessos.models.process.property.Name;
import com.limatech.juriprocessos.models.users.entity.Task;
import com.limatech.juriprocessos.models.users.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class ManageTaskDTO {

    private UUID userId;

    private UUID processId;

    private Name name;

    private Description description;

    private LocalDateTime startAt;

    private LocalDateTime finishAt;

    private LocalDateTime deadlineAt;

    public ManageTaskDTO(UUID userId, UUID processId, String name, String description, LocalDateTime startedAt,
                         LocalDateTime finishAt, LocalDateTime deadlineAt) {
        this.userId = userId;
        this.processId = processId;
        this.name = new Name(name);
        this.description = new Description(description);
        this.startAt = startedAt;
        this.finishAt = finishAt;
        this.deadlineAt = deadlineAt;
    }

    public ManageTaskDTO() {

    }

    public Task toEntity() {
        return new Task(this.name.getName(), this.description.getValue(), this.deadlineAt, new User(), new Process());
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getProcessId() {
        return processId;
    }

    public String getName() {
        return name.getName();
    }

    public String getDescription() {
        return description.getValue();
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public LocalDateTime getFinishAt() {
        return finishAt;
    }

    public LocalDateTime getDeadlineAt() {
        return deadlineAt;
    }
}