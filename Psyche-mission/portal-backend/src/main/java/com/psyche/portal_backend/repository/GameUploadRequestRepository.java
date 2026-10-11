package com.psyche.portal_backend.repository;

import java.util.List;
import java.util.Optional;
import com.psyche.portal_backend.model.GameUploadRequest;
import com.psyche.portal_backend.model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameUploadRequestRepository extends JpaRepository<GameUploadRequest, Long> {
    List<GameUploadRequest> findByStatus(RequestStatus status);

    List<GameUploadRequest> findBySubmittedBy_Username(String username);
}