package com.limatech.juriprocessos.models.process;

import com.limatech.juriprocessos.exceptions.users.InvalidPropertyException;

import java.util.Arrays;
import java.util.List;

public class ProcessDegree {

    private String value;

    private List<String> degreesAvaliable = Arrays.asList("0", "1", "2", "3");

    public ProcessDegree(String value) {
        setValue(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        validate(value);

        this.value = value;
    }

    private void validate(String value) {
        if(!degreesAvaliable.contains(value)) {
            throw new InvalidPropertyException("Invalid degree value: " + value);
        }


    }
}
