package com.limatech.juriprocessos.models.process;

public abstract class ProcessProperty {
    private final int maxLength = 70;

    private final int minLength = 5;

    protected int getMaxLength() {
        return maxLength;
    }

    protected int getMinLength() { return minLength; }

    protected boolean hasExceededLength(String property) {
        return property.length() > maxLength;
    }

    protected boolean hasLength(String property) {
        return property.length() < minLength;
    }
}