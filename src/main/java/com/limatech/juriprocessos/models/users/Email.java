package com.limatech.juriprocessos.models.users;

import com.limatech.juriprocessos.exceptions.users.InvalidPropertyException;

import java.util.regex.Pattern;

public class Email extends UserProperty{
    private String address;

    public Email(String address) {

       this.setAddress(address);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        validate(address);

        this.address = address;
    }

    @Override
    public String toString() {
        return address;
    }

    private void validate(String email) {
        if(hasExceededLength(email)) {
            throw new InvalidPropertyException("Email address must have no more than "+ getMaxLength()+" characters");
        }

        if(hasLength(email)) {
            throw new InvalidPropertyException("Email must have at least " + getMinLength() + " characters");
        }

        // Regular expression pattern to validate email addresses
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        // Compile the pattern
        Pattern pattern = Pattern.compile(regex);

        // Check if the email matches the pattern
        if(!pattern.matcher(email).matches()) {
            throw new InvalidPropertyException("Invalid email address");
        }
    }
}
