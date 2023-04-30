package com.limatech.juriprocessos.models.users.entity;

import com.limatech.juriprocessos.models.process.entity.Process;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "_task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime startAt;

    private LocalDateTime finishAt;

    @NotNull
    private LocalDateTime deadlineAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "process_id")
    private Process process;

    public Task(String name, String description, LocalDateTime deadlineAt, User user, Process process) {
        this.name = name;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.deadlineAt = deadlineAt;
        this.user = user;
        this.process = process;
    }

    public Task() {

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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
