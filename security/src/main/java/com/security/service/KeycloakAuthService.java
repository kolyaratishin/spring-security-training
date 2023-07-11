package com.security.service;

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
public class KeycloakAuthService {
    private final RestTemplate restTemplate;

    public List<UserDto> getAllUsers(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        return getResponse("http://localhost:8086/api/v1/demo/users", requestEntity, HttpMethod.GET);
    }

    private <T> T getResponse(String url, HttpEntity<Object> requestEntity, HttpMethod method) {
        ResponseEntity<T> response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        ParameterizedTypeReference<T> typeReference = new ParameterizedTypeReference<>() {};
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
