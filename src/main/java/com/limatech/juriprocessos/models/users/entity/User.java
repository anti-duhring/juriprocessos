package com.limatech.juriprocessos.models.users.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.limatech.juriprocessos.models.process.entity.Group;
import com.limatech.juriprocessos.models.process.entity.Process;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "_user")
public class User implements UserDetails {

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
    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

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

    public void setAuthorities(Role role) {
        this.role = role;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
