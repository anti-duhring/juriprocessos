package com.limatech.juriprocessos.models.process.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.limatech.juriprocessos.models.users.entity.Task;
import com.limatech.juriprocessos.models.users.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "_process")
public class Process {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "process", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<Activity> activities = new ArrayList<>();

    @ManyToMany(mappedBy = "processes")
    @JsonIgnore
    private List<Group> groups = new ArrayList<>();

    @OneToMany(mappedBy = "process", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    @NotEmpty
    private String identifier;

    @NotEmpty
    @Pattern(regexp = "^[A-Z]{2}$", message = "UF must be a valid Brazilian state abbreviation with two letters")
    private String uf;

    @NotEmpty
    private String court;

    @NotEmpty
    private String vara;

    @NotEmpty
    private String degree;

    public Process(
            User user,
            String identifier,
            String uf,
            String court,
            String degree,
            String vara
    ) {
        this.user = user;
        this.identifier = identifier;
        this.uf = uf;
        this.court = court;
        this.degree = degree;
        this.vara = vara;
    }

    public Process() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) { this.id = id; }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCourt() {
        return court;
    }

    public void setCourt(String court) {
        this.court = court;
    }

    public String getDegree() {
        return degree;
    }

    public  void setDegree(String degree) {
        this.degree = degree;
    }


    public String getVara() {
        return vara;
    }

    public void setVara(String vara) {
        this.vara = vara;
    }

    public List<Group> getGroups() {
        return groups;
    }
}
