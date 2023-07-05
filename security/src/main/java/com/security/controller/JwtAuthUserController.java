package com.security.controller;

import com.security.controller.request.LoginRequest;
import com.security.dto.UserDto;
import com.security.service.JwtAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/jwt")
@RequiredArgsConstructor
public class JwtAuthUserController {
    private final JwtAuthService jwtAuthService;

    @PostMapping("/registration")
    public String addNewUser(@RequestBody LoginRequest loginRequest) {
        return jwtAuthService.addNewUser(loginRequest);
    }

    @PostMapping("/token")
    public String generateToken(@RequestBody LoginRequest loginRequest) {
        return jwtAuthService.generateToken(loginRequest);
    }

    @GetMapping
    public List<UserDto> getAllUsers(@RequestHeader("Authorization") String token) {
        return jwtAuthService.getAllUsersBasicAuth(token);
    }
}
