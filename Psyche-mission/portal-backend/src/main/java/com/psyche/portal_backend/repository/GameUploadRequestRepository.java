package com.psyche.portal_backend.repository;

import java.util.Optional;
import com.psyche.portal_backend.model.GameUploadRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameUploadRequestRepository extends JpaRepository<GameUploadRequest, Long> {
    Optional<GameUploadRequest> findByUsername(String username);
}