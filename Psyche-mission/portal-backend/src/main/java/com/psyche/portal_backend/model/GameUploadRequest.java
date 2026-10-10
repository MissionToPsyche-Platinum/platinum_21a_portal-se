package com.psyche.portal_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.psyche.portal_backend.model.dto.GameUploadRequestDTO;
import jakarta.persistence.*;

@Entity
@Table(name="game-upload-requests")
public class GameUploadRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long requestId;

    @OneToOne(optional = true)
    private Game game;

    @ManyToOne
    private User submittedBy;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Column(columnDefinition = "TEXT")
    private String gameJson;

    public GameUploadRequest() {}

    public GameUploadRequest(Game game, User user) {
        this.game = game;
        this.submittedBy = user;
        this.status = RequestStatus.PENDING;
    }

    public long getRequestId() {
        return this.requestId;
    }

    public RequestStatus getRequestStatus() {
        return this.status;
    }

    public void setRequestStatus(RequestStatus status) {
        this.status = status;
    }

    public Game getGame() {
        if (this.game != null) {
            return this.game;
        }

        if (this.gameJson == null || this.gameJson.isBlank()) {
            return null;
        }

        try {
            GameUploadRequestDTO dto = new ObjectMapper().readValue(this.gameJson, GameUploadRequestDTO.class);
            return dto.toGame();
        } catch (Exception e) {
            return null;
        }
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @JsonIgnore
    public String getGameJson() {
        return this.gameJson;
    }

    public void setGameJson(String gameJson) {
        this.gameJson = gameJson;
    }

    public User getSubmittedBy() {
        return this.submittedBy;
    }
}
