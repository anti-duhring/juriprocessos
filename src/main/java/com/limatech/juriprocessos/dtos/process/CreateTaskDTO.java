package com.limatech.juriprocessos.dtos.process;

import com.limatech.juriprocessos.models.process.entity.Process;
import com.limatech.juriprocessos.models.process.property.Description;
import com.limatech.juriprocessos.models.process.property.Name;
import com.limatech.juriprocessos.models.users.entity.Task;
import com.limatech.juriprocessos.models.users.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class CreateTaskDTO {

    private UUID userId;

    private UUID processId;

    private User user;

    private Process process;

    private Name name;

    private Description description;

    private LocalDateTime deadlineAt;

    public CreateTaskDTO(UUID userId, UUID processId, String name, String description, LocalDateTime deadlineAt) {
        this.userId = userId;
        this.processId = processId;
        this.name = new Name(name);
        this.description = new Description(description);
        this.deadlineAt = deadlineAt;
    }

    public Task toEntity() {
        return new Task(
                this.name.getName(),
                this.description.getValue(),
                this.deadlineAt,
                this.user,
                this.process
        );
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getProcessId() {
        return processId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public String getName() {
        return name.getName();
    }

    public String getDescription() {
        return description.getValue();
    }

    public LocalDateTime getDeadlineAt() {
        return deadlineAt;
    }

    public User getUser() {
        return user;
    }

    public Process getProcess() {
        return process;
    }
}
