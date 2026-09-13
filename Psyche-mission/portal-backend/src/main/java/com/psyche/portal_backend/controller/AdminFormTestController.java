package com.psyche.portal_backend.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminFormTestController {

    @PostMapping
    public Map<String, Object> createGame(
            @RequestBody Map<String, Object> game) {

        System.out.println(
                "Received game: " + game
        );

        return game;
    }
}
