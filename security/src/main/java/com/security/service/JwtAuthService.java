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
public class JwtAuthService {
    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8085/jwt";


    public String addNewUser(LoginRequest loginRequest) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(loginRequest);
        return getResponse(baseUrl + "/registration", requestEntity, HttpMethod.POST, new ParameterizedTypeReference<>() {
        });
    }


    public String generateToken(LoginRequest loginRequest) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(loginRequest);
        return getResponse(baseUrl + "/token", requestEntity, HttpMethod.POST, new ParameterizedTypeReference<>() {
        });
    }

    public List<UserDto> getAllUsersBasicAuth(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        return getResponse("http://localhost:8085/user", requestEntity, HttpMethod.GET, new ParameterizedTypeReference<>() {
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
}
