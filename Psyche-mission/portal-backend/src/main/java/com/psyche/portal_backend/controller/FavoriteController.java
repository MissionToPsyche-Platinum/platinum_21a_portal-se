package com.psyche.portal_backend.controller;

import com.psyche.portal_backend.service.FavoriteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "http://localhost:5173")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping("/{username}")
    public List<String> getFavorites(@PathVariable String username) {
        return favoriteService.getFavoriteGameIds(username);
    }

    @PutMapping("/{username}")
    public List<String> saveFavorites(@PathVariable String username, @RequestBody List<String> gameIds) {
        return favoriteService.saveFavoriteGameIds(username, gameIds);
    }
}
