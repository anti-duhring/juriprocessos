package com.limatech.juriprocessos.models.users;

import com.limatech.juriprocessos.exceptions.users.InvalidProperty;

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
        if(hasExceededLength(address)) {
            throw new InvalidProperty("Email address must have no more than "+ getMaxLength()+" characters");
        }

        if (validate(address)) {
            this.address = address;
        } else {
            throw new InvalidProperty("Invalid email address");
        }
    }

    @Override
    public String toString() {
        return address;
    }

    private boolean validate(String email) {
        // Regular expression pattern to validate email addresses
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        // Compile the pattern
        Pattern pattern = Pattern.compile(regex);

        // Check if the email matches the pattern
        return pattern.matcher(email).matches();
    }
}
