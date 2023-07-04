package com.security.controller;

import com.security.controller.request.LoginRequest;
import com.security.dto.UserDto;
import com.security.service.BasicAuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/basic")
@RequiredArgsConstructor
public class BasicAuthUserController {

    private final BasicAuthUserService userService;

    @GetMapping
    public List<UserDto> getAllUsers(@RequestHeader("Authorization") String authorizationHeader) {
        return userService.getAllUsersBasicAuth(authorizationHeader);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.registration(loginRequest));
    }
}
