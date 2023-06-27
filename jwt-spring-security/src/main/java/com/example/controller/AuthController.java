package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jwt")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/registration")
    public String addNewUser(@RequestBody User user){
        return userService.addUser(user);
    }

    @PostMapping("/token")
    public String generateToken(@RequestBody User user){
        return userService.generateToken(user.getUsername());
    }

    @GetMapping("/validation")
    public String validateToken(@RequestParam("token") String token){
        userService.validateToken(token);
        return "Token is valid";
    }
}
