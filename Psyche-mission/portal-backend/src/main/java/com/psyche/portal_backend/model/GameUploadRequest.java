package com.psyche.portal_backend.model;

import jakarta.persistence.*;

@Entity
@Table(name="game-upload-requests")
public class GameUploadRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long requestId;

    @OneToOne
    private Game game;

    @ManyToOne
    private User submittedBy;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

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
}
