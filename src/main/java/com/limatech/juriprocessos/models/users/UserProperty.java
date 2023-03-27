package com.limatech.juriprocessos.models.users;

public abstract class UserProperty {
    private final int maxLength = 70;

    protected int getMaxLength() {
        return maxLength;
    }

    protected boolean hasExceededLength(String property) {
        return property.length() > maxLength;
    }
}

