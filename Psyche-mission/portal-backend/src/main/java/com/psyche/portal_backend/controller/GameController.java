package com.psyche.portal_backend.controller;

import com.psyche.portal_backend.model.Game;
import com.psyche.portal_backend.service.GameService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "http://localhost:5173")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public List<Game> getAllGames() {
        return gameService.getAllGames();
    }

    @PostMapping
    public ResponseEntity<Game> createGame(@RequestBody Game game) {

        System.out.println("Received game: " + game.getTitle());

         Game savedGame = gameService.saveGame(game);

         System.out.println("Saved game ID: " + savedGame.getId());

        return ResponseEntity.ok(savedGame);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable String id) {
        return gameService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
