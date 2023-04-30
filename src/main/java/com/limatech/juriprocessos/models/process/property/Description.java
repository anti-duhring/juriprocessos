package com.limatech.juriprocessos.models.process.property;

import com.limatech.juriprocessos.exceptions.users.InvalidPropertyException;

public class Description extends ProcessProperty{
    private String value;

    private final int maxLength = 255;

    public Description(String value) {

        setValue(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        validate(value);

        this.value = value;
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
