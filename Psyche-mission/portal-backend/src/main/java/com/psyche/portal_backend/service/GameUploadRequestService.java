package com.psyche.portal_backend.service;

import com.psyche.portal_backend.model.GameUploadRequest;
import com.psyche.portal_backend.repository.GameUploadRequestRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class GameUploadRequestService {
    private final GameUploadRequestRepository repository;

    public GameUploadRequestService(GameUploadRequestRepository repository) {
        this.repository = repository;
    }

    public GameUploadRequest getRequestById(Long id) {
        Optional<GameUploadRequest> request = repository.findById(id);

        if (request.isEmpty()) {
            throw new RuntimeException("Request not found");
        }

        return request.get();
    }

    public GameUploadRequest saveRequest(GameUploadRequest request) {
        return repository.save(request);
    }
}