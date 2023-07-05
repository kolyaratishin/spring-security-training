package com.security.controller.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final RestTemplate restTemplate;
    private final Set<String> urisToSkip = Set.of("/api/v1/user/jwt/registration",
            "/api/v1/user/jwt/token");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!urisToSkip.contains(request.getRequestURI())) {
            try {
                String token = request.getHeader("Authorization").substring(7);
                ResponseEntity<String> exchange = restTemplate.exchange("http://localhost:8085/jwt/validation?token=" + token, HttpMethod.GET, null, String.class);
                exchange.getBody();
            } catch (HttpClientErrorException ex) {
                response.sendError(ex.getStatusCode().value());
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
