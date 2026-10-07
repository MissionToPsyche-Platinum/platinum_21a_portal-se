package com.psyche.portal_backend.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "favorites",
        uniqueConstraints = @UniqueConstraint(columnNames = {"username", "game_id"})
)
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(name = "game_id", nullable = false)
    private String gameId;

    public Favorite() {}

    public Favorite(String username, String gameId) {
        this.username = username;
        this.gameId = gameId;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getGameId() {
        return gameId;
    }
}
