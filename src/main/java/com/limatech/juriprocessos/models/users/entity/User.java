package com.limatech.juriprocessos.models.users.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.limatech.juriprocessos.models.process.entity.Group;
import com.limatech.juriprocessos.models.process.entity.Process;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotEmpty
    private String username;

    @NotEmpty
    private String name;

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Process> processes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Group> groups = new ArrayList<>();

    @ManyToMany(mappedBy = "canRead")
    @JsonIgnore
    private List<Group> canRead = new ArrayList<>();

    @ManyToMany(mappedBy = "canWrite")
    @JsonIgnore
    private List<Group> canWrite = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Task> tasks = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public User(String username, String name, String email, String password) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User() {

    }
}
