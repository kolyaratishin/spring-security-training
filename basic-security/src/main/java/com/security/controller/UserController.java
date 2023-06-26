package com.security.controller;

import com.security.controller.request.UserDto;
import com.security.controller.response.UserResponse;
import com.security.model.User;
import com.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();

        // Аутентифікація користувача
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails == null
                || !passwordEncoder.matches(password, userDetails.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        // Успішна аутентифікація
        // В цьому прикладі ми не генеруємо токен, оскільки використовуємо Basic автентифікацію
        // Ви можете додати код для генерації та повернення токена в залежності від ваших потреб

        // Повертаємо відповідь з успішним статусом
        return ResponseEntity.ok("Successfully logged in");
    }

    @PostMapping("/registration")
    public ResponseEntity<UserResponse> registration(@RequestBody UserDto userDto) {
        if (userService.getUserByUsername(userDto.getUsername()).isEmpty()) {
            User registeredUser = userService.register(userDto);
            UserResponse userResponse = new UserResponse();
            userResponse.setId(registeredUser.getId());
            userResponse.setUsername(registeredUser.getUsername());
            userResponse.setRole(registeredUser.getRole());
            return ResponseEntity.ok(userResponse);
        }
        throw new RuntimeException("Such user with username=" + userDto.getUsername() + " already exists");
    }
}
