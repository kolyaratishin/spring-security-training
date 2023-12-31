package com.example.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private final String type = "Bearer";
    private String refreshToken;
    private Long id;
    private String username;
    private List<String> roles;
}
