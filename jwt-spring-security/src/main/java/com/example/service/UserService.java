package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public User register(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public String generateToken(String username){
        return jwtService.generateToken(username);
    }

    public Boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
}
