package com.limatech.juriprocessos.models.process;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.UUID;


@Entity
@Table(name = "_activity")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_id")
    @JsonIgnore
    private Process process;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotEmpty
    private String type;

    @NotEmpty
    private String date;

    public Activity(Process process, String name, String description, String type, String date) {
        this.process = process;
        this.name = name;
        this.description = description;
        this.type = type;
        this.date = date;
    }

    public Activity() {

    }

    public Process getProcess() {
        return process;
    }

    public UUID getId() {
        return id;
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

    public String getDate() {
        return date;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
