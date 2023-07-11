package com.example.controller;

import com.example.controller.dto.UserDto;
import com.example.model.User;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jwt")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/registration")
    public ResponseEntity<?> addNewUser(@RequestBody User user) {
        if (userService.getUserByUsername(user.getUsername()).isEmpty()) {
            User registeredUser = userService.register(user);
            UserDto userResponse = new UserDto();
            userResponse.setUsername(registeredUser.getUsername());
            userResponse.setRole(registeredUser.getRole());
            return ResponseEntity.ok(userResponse);
        }
        return new ResponseEntity<>("Such user with username=" + user.getUsername() + " already exists", HttpStatus.FORBIDDEN);
    }

    @PostMapping("/token")
    public String generateToken(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            return userService.generateToken(user.getUsername());
        }
        throw new RuntimeException("User invalid access");
    }

    @GetMapping("/validation")
    public String validateToken(@RequestParam("token") String token) {
        userService.validateToken(token);
        return "Token is valid";
    }
}
