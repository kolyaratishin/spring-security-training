package com.security.service;

import com.security.controller.request.LoginRequest;
import com.security.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicAuthUserService {

    private final RestTemplate restTemplate;

    public List<UserDto> getAllUsersBasicAuth(String authorizationHeader) {
        String url = "http://localhost:8084/api/user";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authorizationHeader);
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        // Створення об'єкту HttpEntity з тілом та заголовками
        ResponseEntity<UserDto[]> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, UserDto[].class);

        if (response.getStatusCode().is2xxSuccessful()) {
            UserDto[] users = response.getBody();
            return Arrays.stream(users).toList();
        } else {
            throw new RuntimeException("Bad creds : " + response.getStatusCode());
        }
    }

    public String login(LoginRequest loginRequest) {
        String url = "http://localhost:8084/api/user/login";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        // Створення об'єкту HttpEntity з тілом та заголовками
        HttpEntity<LoginRequest> requestEntity = new HttpEntity<>(loginRequest, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            return responseBody;
        } else {
            throw new RuntimeException("Bad Creds :");
        }
    }

    public String registration(LoginRequest loginRequest) {
        String url = "http://localhost:8084/api/user/registration";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        // Створення об'єкту HttpEntity з тілом та заголовками
        HttpEntity<LoginRequest> requestEntity = new HttpEntity<>(loginRequest, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            return responseBody;
        } else {
            throw new RuntimeException("Bad Creds :");
        }
    }
}
