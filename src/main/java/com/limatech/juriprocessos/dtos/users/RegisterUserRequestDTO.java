package com.limatech.juriprocessos.dtos.users;

import com.limatech.juriprocessos.models.users.property.Email;
import com.limatech.juriprocessos.models.users.property.Name;
import com.limatech.juriprocessos.models.users.entity.User;
import com.limatech.juriprocessos.models.users.property.Username;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class RegisterUserRequestDTO {

    private Username username;


    private Name name;


    private Email email;


    private String password;


    public RegisterUserRequestDTO(String username, String name, String email, String password) {
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

        return new User(username.getName(), name.getName(), email.getAddress(), new BCryptPasswordEncoder().encode(password));
    }
}
