package com.psyche.portal_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/csrf")
    public Map<String, String> csrf(CsrfToken token) {
        return Map.of("headerName", token.getHeaderName(), "token", token.getToken());
    }

    @GetMapping("/me")
    public ResponseEntity<?> currentUser(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {

            return ResponseEntity.status(401).build();
        }

        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(authority ->
                    authority.getAuthority().equals("ROLE_ADMIN"));

        return ResponseEntity.ok(Map.of("username", authentication.getName(), "role", isAdmin ? "admin" : "user"));
    }
}