package com.psyche.portal_backend.service;

import com.psyche.portal_backend.model.Favorite;
import com.psyche.portal_backend.repository.FavoriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public List<String> getFavoriteGameIds(String username) {
        List<String> gameIds = new ArrayList<>();
        for (Favorite favorite : favoriteRepository.findByUsername(username)) {
            gameIds.add(favorite.getGameId());
        }
        return gameIds;
    }

    @Transactional
    public List<String> saveFavoriteGameIds(String username, List<String> gameIds) {
        favoriteRepository.deleteByUsername(username);

        if (gameIds != null) {
            for (String gameId : gameIds) {
                favoriteRepository.save(new Favorite(username, gameId));
            }
        }

        return getFavoriteGameIds(username);
    }
}
