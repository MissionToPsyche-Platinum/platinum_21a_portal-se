package com.psyche.portal_backend.repository;

import com.psyche.portal_backend.model.GameUploadRequest;
import com.psyche.portal_backend.model.RequestStatus;
import com.psyche.portal_backend.model.User;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class GameUploadRequestRepositoryTest {

    @Autowired
    private GameUploadRequestRepository gameUploadRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveGameUploadRequest() {

        GameUploadRequest request = new GameUploadRequest();
        request.setRequestStatus(RequestStatus.PENDING);

        GameUploadRequest savedRequest =
                gameUploadRequestRepository.save(request);

        assertNotNull(savedRequest.getRequestId());
    }

    @Test
    void shouldFindGameUploadRequestById() {
        GameUploadRequest request = new GameUploadRequest();

        request.setRequestStatus(RequestStatus.PENDING);

        GameUploadRequest savedRequest = gameUploadRequestRepository.save(request);
        Optional<GameUploadRequest> result = gameUploadRequestRepository.findById(savedRequest.getRequestId());

        assertTrue(result.isPresent());
        assertEquals(RequestStatus.PENDING, result.get().getRequestStatus());
    }

    @Test
    void shouldFindAllGameUploadRequests() {
        GameUploadRequest req1 = new GameUploadRequest();
        GameUploadRequest req2 = new GameUploadRequest();

        req1.setRequestStatus(RequestStatus.PENDING);
        req2.setRequestStatus(RequestStatus.DENIED);

        gameUploadRequestRepository.save(req1);
        gameUploadRequestRepository.save(req2);

        List<GameUploadRequest> results = gameUploadRequestRepository.findAll();

        assertEquals(2, results.size());
    }

    @Test
    void shouldFindRequestByStatus() {
        GameUploadRequest req1 = new GameUploadRequest();
        GameUploadRequest req2 = new GameUploadRequest();
        GameUploadRequest req3 = new GameUploadRequest();

        req1.setRequestStatus(RequestStatus.PENDING);
        req2.setRequestStatus(RequestStatus.PENDING);
        req3.setRequestStatus(RequestStatus.DENIED);

        gameUploadRequestRepository.save(req1);
        gameUploadRequestRepository.save(req2);
        gameUploadRequestRepository.save(req3);

        List<GameUploadRequest> results = gameUploadRequestRepository.findByStatus(RequestStatus.PENDING);

        assertEquals(2, results.size());

        boolean allPending = true;

        for (GameUploadRequest req : results) {
            if (req.getRequestStatus() != RequestStatus.PENDING) {
                allPending = false;
                break;
            }
        }

        assertTrue(allPending);
    }

    @Test
    void shouldReturnEmptyListWithNoPendingRequests() {
        GameUploadRequest req = new GameUploadRequest();

        req.setRequestStatus(RequestStatus.DENIED);
        gameUploadRequestRepository.save(req);

        List<GameUploadRequest> results = gameUploadRequestRepository.findByStatus(RequestStatus.PENDING);

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void shouldFindRequestsByUsername() {
        User user = new User("testuser", "test@example.com", "password");
        User savedUser = userRepository.save(user);
        GameUploadRequest req1 = new GameUploadRequest(null, savedUser);
        GameUploadRequest req2 = new GameUploadRequest(null, savedUser);

        gameUploadRequestRepository.save(req1);
        gameUploadRequestRepository.save(req2);

        List<GameUploadRequest> results = gameUploadRequestRepository.findBySubmittedBy_Username(savedUser.getUsername());

        assertEquals(2, results.size());

        boolean allBelongToUser = true;

        for (GameUploadRequest req : results) {
            if (!req.getSubmittedBy().getUsername().equals(savedUser.getUsername())) {
                allBelongToUser = false;
                break;
            }
        }

        assertTrue(allBelongToUser);
    }
}
