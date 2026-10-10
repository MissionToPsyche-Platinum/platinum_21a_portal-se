package com.psyche.portal_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public Map<String, String> root() {
        return Map.of(
                "status", "ok",
                "message", "Psyche Portal API. Use the Vue app at http://localhost:5173/",
                "games", "/api/games",
                "test", "/api/test/status"
        );
    }
}
