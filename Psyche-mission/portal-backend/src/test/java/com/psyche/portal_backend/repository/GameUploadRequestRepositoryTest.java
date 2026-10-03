package com.psyche.portal_backend.repository;

import com.psyche.portal_backend.model.GameUploadRequest;
import com.psyche.portal_backend.model.RequestStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameUploadRequestRepositoryTest {

    @Autowired
    private GameUploadRequestRepository gameUploadRequestRepository;

    @Test
    void shouldSaveGameUploadRequest() {

        GameUploadRequest request = new GameUploadRequest();
        request.setStatus(RequestStatus.PENDING);

        GameUploadRequest savedRequest =
                gameUploadRequestRepository.save(request);

        assertNotNull(savedRequest.getRequestId());
    }
}
