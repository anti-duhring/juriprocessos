package com.limatech.juriprocessos.dtos.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.limatech.juriprocessos.models.users.entity.User;

public class LoginUserResponseDTO {

    @JsonProperty("token")
    private String token;
    @JsonProperty("user")
    private User user;

    public LoginUserResponseDTO(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public LoginUserResponseDTO() {
    }
}
