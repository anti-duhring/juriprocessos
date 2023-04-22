package com.limatech.juriprocessos.models.process;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "_group")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID  id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @ManyToMany(mappedBy = "groups", cascade = CascadeType.ALL)
    private List<Process> processes = new ArrayList<>();

    public Group(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Group() {

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

    public List<Process> getProcesses() {
        return processes;
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

    public void addProcess(Process process) {
        processes.add(process);
    }

    public void removeProcess(Process process) {
        processes.remove(process);
    }
}
