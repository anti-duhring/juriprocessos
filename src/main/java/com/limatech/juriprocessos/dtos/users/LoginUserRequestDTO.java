package com.limatech.juriprocessos.dtos.users;

import com.limatech.juriprocessos.models.users.property.Username;

public class LoginUserRequestDTO {

    public Username username;
    public String password;

    public LoginUserRequestDTO(Username username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username.getName();
    }

    public void setUsername(String username) {
        this.username.setName(username);
    }
}
