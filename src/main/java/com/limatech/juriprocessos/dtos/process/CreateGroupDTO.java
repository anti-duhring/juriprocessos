package com.limatech.juriprocessos.dtos.process;

import com.limatech.juriprocessos.models.process.Description;
import com.limatech.juriprocessos.models.process.Group;
import com.limatech.juriprocessos.models.process.Name;

public class CreateGroupDTO {

    private Name name;

    private Description description;

    public CreateGroupDTO(String name, String description) {
        this.name = new Name(name);
        this.description = new Description(description);
    }

    public Group toEntity() {
        return new Group(this.name.getName(), this.description.getValue());
    }
}
