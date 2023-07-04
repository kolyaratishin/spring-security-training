package com.security.service;

import com.security.controller.request.LoginRequest;
import com.security.dto.UserDto;
import com.security.exceptions.ForbiddenException;
import com.security.exceptions.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicAuthUserService {

    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8084/api/user";

    public List<UserDto> getAllUsersBasicAuth(String authorizationHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authorizationHeader);
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        return getResponse(baseUrl, requestEntity, HttpMethod.GET, new ParameterizedTypeReference<>() {
        });
    }

    private <T> T getResponse(String url, HttpEntity<Object> requestEntity, HttpMethod method, ParameterizedTypeReference<T> typeReference) {
        ResponseEntity<T> response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        try {
            response = restTemplate.exchange(url, method, requestEntity, typeReference);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new UnauthorizedException(ex.getMessage());
            } else if (ex.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new ForbiddenException(ex.getMessage());
            }
        }
        return response.getBody();
    }

    public String login(LoginRequest loginRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<Object> requestEntity = new HttpEntity<>(loginRequest, headers);
        return getResponse(baseUrl + "/login", requestEntity, HttpMethod.POST, new ParameterizedTypeReference<>() {
        });
    }

    public String registration(LoginRequest loginRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<Object> requestEntity = new HttpEntity<>(loginRequest, headers);
        return getResponse(baseUrl + "/registration", requestEntity, HttpMethod.POST, new ParameterizedTypeReference<>() {
        });
    }
}
