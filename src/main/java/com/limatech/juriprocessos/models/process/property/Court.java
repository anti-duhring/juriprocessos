package com.limatech.juriprocessos.models.process.property;

import com.limatech.juriprocessos.exceptions.users.InvalidPropertyException;

public class Court {

    private String name;

    public Court(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        validate(name);

        this.name = name;
    }

    private void validate(String value) {
        if (value == null || value.isEmpty()) {
            throw new InvalidPropertyException("Court must not be null");
        }
    }
}
