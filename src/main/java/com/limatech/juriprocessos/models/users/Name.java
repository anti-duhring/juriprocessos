package com.limatech.juriprocessos.models.users;

import com.limatech.juriprocessos.exceptions.users.InvalidProperty;

public class Name extends UserProperty{

    private String name;

    public Name(String name) {

        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(hasExceededLength(name)) {
            throw new InvalidProperty("Name must have no more than "+ getMaxLength()+" characters");
        }

        this.name = name;
    }
}
