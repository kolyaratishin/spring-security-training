package com.security.controller;

import com.security.dto.UserDto;
import com.security.service.KeycloakAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/keycloak")
@RequiredArgsConstructor
public class KeycloakAuthController {

    private final KeycloakAuthService keycloakAuthService;

    @GetMapping
    public List<UserDto> getAllUsers(@RequestHeader(name = "Authorization") String token) {
        return keycloakAuthService.getAllUsers(token);
    }
}
