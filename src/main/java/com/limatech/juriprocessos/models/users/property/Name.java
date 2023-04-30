package com.limatech.juriprocessos.models.users.property;

import com.limatech.juriprocessos.exceptions.users.InvalidPropertyException;

public class Name extends UserProperty{

    private String name;

    public Name(String name) {

        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        validate(name);

        this.name = name;
    }

    public void validate(String value) {
        if(hasLength(value)) {
            throw new InvalidPropertyException("Name must have at least " + getMinLength() + " characters");
        }

        if(hasExceededLength(value)) {
            throw new InvalidPropertyException("Name must have no more than "+ getMaxLength()+" characters");
        }
    }
}
