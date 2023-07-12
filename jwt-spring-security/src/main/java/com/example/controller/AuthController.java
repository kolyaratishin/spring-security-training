package com.example.controller;

import com.example.config.CustomUserDetails;
import com.example.controller.dto.UserDto;
import com.example.controller.request.LoginRequest;
import com.example.controller.response.JwtResponse;
import com.example.model.RefreshToken;
import com.example.model.User;
import com.example.service.JwtService;
import com.example.service.RefreshTokenService;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/jwt")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

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
    public ResponseEntity<?> generateToken(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String jwt = jwtService.generateToken(userDetails.getUsername());

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .toList();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
                userDetails.getUsername(), roles));
    }

    @GetMapping("/validation")
    public String validateToken(@RequestParam("token") String token) {
        userService.validateToken(token);
        return "Token is valid";
    }
}
