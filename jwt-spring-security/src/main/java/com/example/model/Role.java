package com.example.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    ADMIN(Set.of("USER","ADMIN")),
    USER(Set.of("USER"));
    private final Set<String> permissions;
    Role(Set<String> permissions){this.permissions=permissions;}

    public Set<String> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities(){
        return getPermissions().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
