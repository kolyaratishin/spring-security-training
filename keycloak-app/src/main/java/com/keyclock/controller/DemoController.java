package com.keyclock.controller;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    @GetMapping
    @PreAuthorize("hasRole('client_user')")
    public String hello(){
        return "Hello from Spring boot & Keycloak";
    }

    @GetMapping("/hello-2")
    @PreAuthorize("hasRole('client_admin')")
    public String hello2(){
        return "Hello from Spring boot & Keycloak - ADMIN";
    }

    @GetMapping("/users")
    public List<UserRepresentation> getUsers(){
        Keycloak keycloak = Keycloak.getInstance(
                "http://localhost:8080",
                "master",
                "admin",
                "admin",
                "admin-cli");
        var users = keycloak.realm("Kolya").users().list();
        return users;
    }
}
