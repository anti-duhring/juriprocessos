package com.limatech.juriprocessos.models.process;

import com.limatech.juriprocessos.exceptions.users.InvalidPropertyException;
import com.limatech.juriprocessos.models.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

@Entity
@Table(name = "_process")
public class Process {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotEmpty
    private String identifier;

    @NotEmpty
    @Pattern(regexp = "^[A-Z]{2}$", message = "UF must be a valid Brazilian state abbreviation with two letters")
    private String uf;

    @NotEmpty
    private String court;

    public Process(User user, String identifier, String uf, String court) {
        this.user = user;
        this.identifier = identifier;
        this.uf = uf;
        this.court = court;
    }

    public Process() {

    }

    public UUID getUuid() {
        return uuid;
    }

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
}
