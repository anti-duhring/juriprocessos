package com.limatech.juriprocessos.models.process;

import com.limatech.juriprocessos.exceptions.users.InvalidPropertyException;

import java.util.Arrays;
import java.util.List;

public class UF {
    private String value;

    public UF(String value) {
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
        if (value == null || value.length() != 2) {
            throw new InvalidPropertyException("UF invalid");
        }
        String uppercaseUF = value.toUpperCase();
        List<String> brazilianUFs = Arrays.asList("AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT",
                "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO");

        if(!brazilianUFs.contains(uppercaseUF)) {
            throw new InvalidPropertyException("UF invalid");
        }
    }
}
