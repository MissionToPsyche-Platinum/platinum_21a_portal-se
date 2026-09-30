package com.psyche.portal_backend.service;

import com.psyche.portal_backend.model.GameUploadRequest;
import com.psyche.portal_backend.repository.GameUploadRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameUploadRequestService {

    private final GameUploadRequestRepository gameUploadRequestRepository;

    public GameUploadRequestService(
            GameUploadRequestRepository gameUploadRequestRepository) {
        this.gameUploadRequestRepository = gameUploadRequestRepository;
    }

    public GameUploadRequest saveRequest(GameUploadRequest request) {
        return gameUploadRequestRepository.save(request);
    }

    public List<GameUploadRequest> getAllRequests() {
        return gameUploadRequestRepository.findAll();
    }

    public Optional<GameUploadRequest> getRequestById(Long requestId) {
        return gameUploadRequestRepository.findById(requestId);
    }

    public Optional<GameUploadRequest> getRequestByUsername(String username) {
        return gameUploadRequestRepository.findBySubmittedByUsername(username);
    }
}