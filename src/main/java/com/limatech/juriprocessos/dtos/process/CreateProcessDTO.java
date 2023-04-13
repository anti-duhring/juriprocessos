package com.limatech.juriprocessos.dtos.process;

import com.limatech.juriprocessos.models.process.Court;
import com.limatech.juriprocessos.models.process.Process;
import com.limatech.juriprocessos.models.process.ProcessIdentifier;
import com.limatech.juriprocessos.models.process.UF;
import com.limatech.juriprocessos.models.users.User;

public class CreateProcessDTO {

    private User user;

    private ProcessIdentifier identifier;

    private UF uf;

    private Court court;

    public CreateProcessDTO(User user, String identifier, String uf, String court) {
        this.user = user;
        this.identifier = new ProcessIdentifier(identifier);
        this.uf = new UF(uf);
        this.court = new Court(court);
    }

    public Process toEntity() {
        return new Process(this.user, this.identifier.getIdentifier(), this.uf.getValue(), this.court.getName());
    }
}
