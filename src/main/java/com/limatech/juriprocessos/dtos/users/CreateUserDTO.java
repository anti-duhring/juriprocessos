package com.limatech.juriprocessos.dtos.users;

import com.limatech.juriprocessos.models.users.Email;
import com.limatech.juriprocessos.models.users.Name;
import com.limatech.juriprocessos.models.users.User;
import com.limatech.juriprocessos.models.users.Username;
import jakarta.validation.constraints.NotEmpty;

public class CreateUserDTO {
    @NotEmpty
    private Username username;

    @NotEmpty
    private Name name;

    @NotEmpty
    private Email email;

    @NotEmpty
    private String password;

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

    public User toUserEntity() {

        return new User(username.getName(), name.getName(), email.getAddress(), password);
    }
}
