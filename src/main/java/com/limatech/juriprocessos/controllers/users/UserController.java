package com.limatech.juriprocessos.controllers.users;

import com.limatech.juriprocessos.dtos.users.CreateUserDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.url_base}/users)")
public class UserController {

    @PostMapping
    public ResponseEntity addUser(
            @RequestBody @Valid CreateUserDTO userDTO
    ) {
        return ResponseEntity.ok(userDTO);
    }
}
