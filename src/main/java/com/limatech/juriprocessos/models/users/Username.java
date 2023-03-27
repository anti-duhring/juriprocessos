package com.limatech.juriprocessos.models.users;

import com.limatech.juriprocessos.exceptions.users.InvalidProperty;

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
            throw new InvalidProperty("Username must have no more than "+ getMaxLength()+" characters");
        }

        if(validate(name)) {
            this.name = name;
        } else {
            throw new InvalidProperty("Invalid username");
        }
    }

    public boolean validate(String value) {
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

