package com.limatech.juriprocessos.controllers.users;

import com.limatech.juriprocessos.dtos.users.CreateUserDTO;
import com.limatech.juriprocessos.models.users.User;
import com.limatech.juriprocessos.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.url_base}/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity addUser(
            @RequestBody @Valid CreateUserDTO userDTO
    ) {
        User user = userService.createUser(userDTO);
        return ResponseEntity.ok(user);
    }
}
