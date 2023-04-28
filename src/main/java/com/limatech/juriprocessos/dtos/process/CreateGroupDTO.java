package com.limatech.juriprocessos.dtos.process;

import com.limatech.juriprocessos.models.process.Description;
import com.limatech.juriprocessos.models.process.Group;
import com.limatech.juriprocessos.models.process.Name;
import com.limatech.juriprocessos.models.users.User;

import java.util.UUID;

public class CreateGroupDTO {

    private UUID userId;

    private User user;

    private Name name;

    private Description description;

    public CreateGroupDTO(UUID userId, String name, String description) {
        this.userId = userId;
        this.name = new Name(name);
        this.description = new Description(description);
    }

    public Group toEntity() {
        return new Group(this.user, this.name.getName(), this.description.getValue());
    }

    public String getName() {
        return name.getName();
    }

    public String getDescription() {
        return description.getValue();
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
