package com.psyche.portal_backend.controller;

import com.psyche.portal_backend.model.Game;
import com.psyche.portal_backend.service.GameService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/games")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/game")
    public Map<String, Object> createGame(
            @RequestBody Map<String, Object> game) {

        System.out.println("Received game: " + game);

        return game;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable Long id) {
        return gameService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
