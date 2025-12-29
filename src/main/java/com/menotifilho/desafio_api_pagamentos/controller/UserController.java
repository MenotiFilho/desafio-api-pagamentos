package com.menotifilho.desafio_api_pagamentos.controller;

import com.menotifilho.desafio_api_pagamentos.dto.UserDTO;
import com.menotifilho.desafio_api_pagamentos.dto.UserResponseDTO;
import com.menotifilho.desafio_api_pagamentos.model.User;
import com.menotifilho.desafio_api_pagamentos.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserDTO userDTO) {
        User newUser = userService.createUser(userDTO);
        UserResponseDTO response = new UserResponseDTO(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        return ResponseEntity.ok(userService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.findUserById(id));
    }

}
