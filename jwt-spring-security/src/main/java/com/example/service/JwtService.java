package com.example.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {
    private UserDetailsService userDetailsService;

    public boolean validateToken(String token) {
        final String usernameFromJwt = extractUsername(token);
        return Optional.ofNullable(userDetailsService.loadUserByUsername(usernameFromJwt)).isPresent() && !isTokenExpired(token);
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        claims.put("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 30 * 1))
                .signWith(getSignKey(), SignatureAlgorithm.RS256).compact();
    }

    private Key getSignKey() {
        String privateKeyFilePath = "C:\\D_disk\\spring-security-training\\spring-security-training\\jwt-spring-security\\private-key.txt";
        try {
            byte[] privateKeyData = getKeyData(privateKeyFilePath, true);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyData);
            return keyFactory.generatePrivate(keySpec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Failed to read private key", e);
        }
    }

    private Key getPublicKey() {
        String privateKeyFilePath = "C:\\D_disk\\spring-security-training\\spring-security-training\\jwt-spring-security\\public-key.txt";
        try {
            byte[] privateKeyData = getKeyData(privateKeyFilePath, false);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(privateKeyData);
            return keyFactory.generatePublic(keySpec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Failed to read public key", e);
        }
    }

    private byte[] getKeyData(String privateKeyFilePath, boolean isPrivateKey) throws IOException {
        byte[] publicKeyBytes = Files.readAllBytes(Paths.get(privateKeyFilePath));
        String header = getHeader(isPrivateKey);
        String footer = getFooter(isPrivateKey);
        String publicKeyPem = new String(publicKeyBytes)
                .replaceAll("\\n", "")
                .replaceAll("\\r", "")
                .replace(header, "")
                .replace(footer, "");

        return Base64.getDecoder().decode(publicKeyPem);
    }

    private String getHeader(boolean isPrivateKey) {
        if (isPrivateKey) {
            return "-----BEGIN PRIVATE KEY-----";
        } else {
            return "-----BEGIN PUBLIC KEY-----";
        }
    }

    private String getFooter(boolean isPrivateKey) {
        if (isPrivateKey) {
            return "-----END PRIVATE KEY-----";
        } else {
            return "-----END PUBLIC KEY-----";
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getPublicKey()).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
