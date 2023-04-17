package com.limatech.juriprocessos.dtos.users;

import com.limatech.juriprocessos.models.users.Email;
import com.limatech.juriprocessos.models.users.Name;
import com.limatech.juriprocessos.models.users.User;
import com.limatech.juriprocessos.models.users.Username;

public class CreateUserDTO {

    private Username username;


    private Name name;


    private Email email;


    private String password;

    public CreateUserDTO(String username, String name, String email, String password) {
        this.username = new Username(username);
        this.name = new Name(name);
        this.email = new Email(email);
        this.password = password;
    }

    public String getUsername() {

        return username.getName();
    }

    public void setUsername(String username) {

        this.username.setName(username);
    }

    public String getName() {

        return name.getName();
    }

    public void setName(String name) {

        this.name.setName(name);
    }

    public String getEmail() {

        return email.getAddress();
    }

    public void setEmail(String email) {

        this.email.setAddress(email);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User toEntity() {

        return new User(username.getName(), name.getName(), email.getAddress(), password);
    }
}
