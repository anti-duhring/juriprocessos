package com.limatech.juriprocessos.models.process;

import com.limatech.juriprocessos.models.users.User;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "group_process",
            joinColumns = @JoinColumn(name = "group_id",  referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "process_id", referencedColumnName = "id")
    )
    private List<Process> processes = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "group_can_read",
                joinColumns = @JoinColumn(name = "group_id",  referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    private List<User> canRead = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "group_can_read",
            joinColumns = @JoinColumn(name = "group_id",  referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    private List<User> canWrite = new ArrayList<>();

    public Group(User user, String name, String description) {
        this.user = user;
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

    public void addCanRead(User user) {
        if(!canRead.contains(user)) {
            canRead.add(user);
        }
    }

    public void removeCanRead(User user) {
        canRead.remove(user);
    }

    public void addCanWrite(User user) {
        if(!canWrite.contains(user)) {
            canWrite.add(user);
        }
    }

    public void removeCanWrite(User user) {
        canWrite.remove(user);
    }

    public List<User> getCanRead() {
        return canRead;
    }

    public List<User> getCanWrite() {
        return canWrite;
    }

}
