package com.psyche.portal_backend.model;

import java.util.concurrent.atomic.AtomicInteger;

public class GameUploadRequest {
    private static final AtomicInteger idGenerator = new AtomicInteger(1);
    private final int requestId;
    private Game game;
    private User submittedBy;
    private RequestStatus status;

    public GameUploadRequest(Game game, User user) {
        this.requestId = idGenerator.getAndIncrement();
        this.game = game;
        this.submittedBy = user;
        this.status = RequestStatus.PENDING;
    }

    public int getRequestId() {
        return this.requestId;
    }

    public RequestStatus getRequestStatus() {
        return this.status;
    }

    public void setRequestStatus(RequestStatus status) {
        this.status = status;
    }
}
