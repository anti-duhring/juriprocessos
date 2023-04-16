package com.limatech.juriprocessos.dtos.process;

import com.limatech.juriprocessos.models.process.*;
import com.limatech.juriprocessos.models.process.Process;
import com.limatech.juriprocessos.models.users.User;

import java.util.UUID;

public class CreateProcessDTO {

    private User user;

    private UUID userId;

    private ProcessIdentifier identifier;

    private UF uf;

    private Court court;

    private ProcessDegree degree;

    private String vara;

    public CreateProcessDTO(UUID userId, String identifier, String uf, String court, String degree, String vara) {
        this.userId = userId;
        this.identifier = new ProcessIdentifier(identifier);
        this.uf = new UF(uf);
        this.court = new Court(court);
        this.degree = new ProcessDegree(degree);
        this.vara = vara;
    }

    public CreateProcessDTO() {

    }

    public Process toEntity() {
        return new Process(
                this.user,
                this.identifier.getIdentifier(),
                this.uf.getValue(),
                this.court.getName(),
                this.degree.getValue(),
                this.vara
        );
    }


    public String getIdentifier() {
        return identifier.getIdentifier();
    }

    public String getUf() {
        return uf.getValue();
    }

    public String getCourt() {
        return court.getName();
    }

    public String getDegree() {
        return degree.getValue();
    }

    public String getVara() {
        return vara;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UUID getUserId() {
        return userId;
    }
}
