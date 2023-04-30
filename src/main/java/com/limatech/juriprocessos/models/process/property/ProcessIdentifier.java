package com.limatech.juriprocessos.models.process.property;

import com.limatech.juriprocessos.exceptions.users.InvalidPropertyException;

public class ProcessIdentifier {
    private String identifier;

    public ProcessIdentifier(String identifier) {
        setIdentifier(identifier);
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        validate(identifier);

        this.identifier = identifier;
    }

    private void validate(String value) {
        if (value == null) {
            throw new InvalidPropertyException("Process identifier must not be null");
        }
    }
}

