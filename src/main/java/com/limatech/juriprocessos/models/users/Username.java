package com.limatech.juriprocessos.models.users;

import com.limatech.juriprocessos.exceptions.users.InvalidPropertyException;

public class Username extends UserProperty{
    private String name;

    public Username(String name) {

        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(hasExceededLength(name)) {
            throw new InvalidPropertyException("Username must have no more than "+ getMaxLength()+" characters");
        }

        if(validate(name)) {
            this.name = name;
        } else {
            throw new InvalidPropertyException("Invalid username");
        }
    }

    public boolean validate(String value) {
        if(hasLength(value)) {
            throw new InvalidPropertyException("Username must have at least " + getMinLength() + " characters");
        }

        // Check for empty spaces
        if (value.contains(" ")) {
            return false;
        }

        // Check for invalid characters
        for (char c : value.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != '_' && c != '.') {
                return false;
            }
        }

        return true;
    }
}

