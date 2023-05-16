package com.limatech.juriprocessos.controllers.users;

import com.limatech.juriprocessos.dtos.users.LoginUserResponseDTO;
import com.limatech.juriprocessos.dtos.users.RegisterUserRequestDTO;
import com.limatech.juriprocessos.dtos.users.LoginUserRequestDTO;
import com.limatech.juriprocessos.models.users.entity.User;
import com.limatech.juriprocessos.services.misc.JwtService;
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

    @Autowired
    JwtService jwtService;

    @PostMapping(path = "/auth/register")
    public ResponseEntity<LoginUserResponseDTO> addUser(
            @RequestBody @Valid RegisterUserRequestDTO userDTO
    ) {
        User user = userService.createUser(userDTO);
        String token = jwtService.generateToken(user);

        LoginUserResponseDTO loginUserResponseDTO = new LoginUserResponseDTO(token, user);

        return ResponseEntity.ok(loginUserResponseDTO);
    }

    @PostMapping(path = "/auth/login")
    public ResponseEntity login(
            @RequestBody @Valid LoginUserRequestDTO userDTO
            ) {
        User user = userService.authenticateUser(userDTO);
        String token = jwtService.generateToken(user);

        LoginUserResponseDTO loginUserResponseDTO = new LoginUserResponseDTO(token, user);

        return ResponseEntity.ok(loginUserResponseDTO);

    }

    @PutMapping(path = "/{id}")
    public ResponseEntity updateUser(
            @PathVariable("id") UUID id,
            @RequestBody @Valid RegisterUserRequestDTO userDTO
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
