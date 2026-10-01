package com.psyche.portal_backend.repository;

import java.util.Optional;
import com.psyche.portal_backend.model.GameUploadRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.psyche.portal_backend.model.RequestStatus;

public interface GameUploadRequestRepository extends JpaRepository<GameUploadRequest, Long> {
    Optional<GameUploadRequest> findBySubmittedByUsername(String username);

    List<GameUploadRequest> findByStatus(RequestStatus status);
}