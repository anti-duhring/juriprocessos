package com.limatech.juriprocessos.dtos.process;

import com.limatech.juriprocessos.models.process.*;
import com.limatech.juriprocessos.models.process.Process;
import com.limatech.juriprocessos.models.users.User;

public class CreateProcessDTO {

    private User user;

    private ProcessIdentifier identifier;

    private UF uf;

    private Court court;

    private ProcessDegree degree;

    public CreateProcessDTO(User user, String identifier, String uf, String court, String degree) {
        this.user = user;
        this.identifier = new ProcessIdentifier(identifier);
        this.uf = new UF(uf);
        this.court = new Court(court);
        this.degree = new ProcessDegree(degree);
    }

    public Process toEntity() {
        return new Process(
                this.user,
                this.identifier.getIdentifier(),
                this.uf.getValue(),
                this.court.getName(),
                this.degree.getValue()
        );
    }
}
