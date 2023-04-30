package com.limatech.juriprocessos.controllers.users;

import com.limatech.juriprocessos.dtos.users.CreateUserDTO;
import com.limatech.juriprocessos.models.users.entity.User;
import com.limatech.juriprocessos.services.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @PutMapping(path = "/{id}")
    public ResponseEntity updateUser(
            @PathVariable("id") UUID id,
            @RequestBody @Valid CreateUserDTO userDTO
    ) {
        User user = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteUser(
            @PathVariable("id") UUID id
    ) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
