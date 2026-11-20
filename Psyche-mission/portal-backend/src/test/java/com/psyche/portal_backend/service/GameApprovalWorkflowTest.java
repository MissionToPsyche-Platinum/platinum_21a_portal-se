
package com.psyche.portal_backend.service;

import com.psyche.portal_backend.model.Game;
import com.psyche.portal_backend.model.GameUploadRequest;
import com.psyche.portal_backend.model.RequestStatus;
import com.psyche.portal_backend.model.dto.GameUploadRequestDTO;
import com.psyche.portal_backend.repository.GameRepository;
import com.psyche.portal_backend.repository.GameUploadRequestRepository;
import com.psyche.portal_backend.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tools.jackson.databind.json.JsonMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameApprovalWorkflowTest {

    @Mock
    private GameUploadRequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameService gameService;

    private GameUploadRequestService service;

    @BeforeEach
    void setUp() {
        service = new GameUploadRequestService(
            requestRepository,
            userRepository,
            gameRepository,
            gameService,
            JsonMapper.builder().build()
        );
    }

    // TEST 1: Approve five requests and deny five.
    @Test
    void tenPendingRequestsAreApprovedOrDenied() throws Exception {

        Map<Long, GameUploadRequest> requests = new HashMap<>();

        // Prepare 10 pending game requests.
        for (long id = 1; id <= 10; id++) {

            GameUploadRequestDTO dto = new GameUploadRequestDTO();
            dto.setTitle("Sprint 12 Game " + id);
            dto.setGenre("Educational");
            dto.setDifficulty("Easy");
            dto.setDescription("Approval workflow test " + id);
            dto.setSrc("https://example.org/game-" + id);

            GameUploadRequest request =
                new GameUploadRequest(null, null);

            request.setGameJson(
                JsonMapper.builder().build().writeValueAsString(dto)
            );

            requests.put(id, request);
        }

        // Simulate finding each request by its ID.
        when(requestRepository.findById(anyLong()))
            .thenAnswer(invocation -> {
                Long id = invocation.getArgument(0);
                return Optional.ofNullable(requests.get(id));
            });

        // Return the request passed to save().
        when(requestRepository.save(any(GameUploadRequest.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // Approve requests 1 through 5.
        for (long id = 1; id <= 5; id++) {

            GameUploadRequest result = service.approveRequest(id);

            assertEquals(
                RequestStatus.APPROVED,
                result.getRequestStatus()
            );

            assertNotNull(result.getGame());
            assertEquals(
                "Sprint 12 Game " + id,
                result.getGame().getTitle()
            );
        }

        // Deny requests 6 through 10.
        for (long id = 6; id <= 10; id++) {

            GameUploadRequest result = service.denyRequest(id);

            assertEquals(
                RequestStatus.DENIED,
                result.getRequestStatus()
            );
        }

        // Exactly five games should be published.
        verify(gameService, times(5))
            .saveGame(any(Game.class));

        // All 10 requests should be saved.
        verify(requestRepository, times(10))
            .save(any(GameUploadRequest.class));
    }

    // TEST 2: A request cannot be approved twice.
    @Test
    void alreadyApprovedRequestCannotBeApprovedAgain() {

        GameUploadRequest request =
            new GameUploadRequest(null, null);

        request.setRequestStatus(RequestStatus.APPROVED);

        when(requestRepository.findById(20L))
            .thenReturn(Optional.of(request));

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> service.approveRequest(20L)
        );

        assertEquals(
            "Request is not pending",
            exception.getMessage()
        );

        verify(requestRepository, never())
            .save(any(GameUploadRequest.class));

        verifyNoInteractions(gameService);
    }

    // TEST 3: A missing request cannot be approved.
    @Test
    void nonexistentRequestCannotBeApproved() {

        when(requestRepository.findById(999L))
            .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> service.approveRequest(999L)
        );

        assertEquals(
            "Request not found",
            exception.getMessage()
        );

        verify(requestRepository, never())
            .save(any(GameUploadRequest.class));

        verifyNoInteractions(gameService);
    }

    // TEST 4: Denial must not publish a game.
    @Test
    void deniedRequestDoesNotPublishGame() {

        GameUploadRequest request =
            new GameUploadRequest(null, null);

        when(requestRepository.findById(30L))
            .thenReturn(Optional.of(request));

        when(requestRepository.save(any(GameUploadRequest.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        GameUploadRequest result = service.denyRequest(30L);

        assertEquals(
            RequestStatus.DENIED,
            result.getRequestStatus()
        );

        verifyNoInteractions(gameService);
    }

    // TEST 5: Approval requires game data.
    @Test
    void requestWithoutGameDataCannotBeApproved() {

        GameUploadRequest request =
            new GameUploadRequest(null, null);

        when(requestRepository.findById(40L))
            .thenReturn(Optional.of(request));

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> service.approveRequest(40L)
        );

        assertEquals(
            "Request has no game data",
            exception.getMessage()
        );

        verify(requestRepository, never())
            .save(any(GameUploadRequest.class));

        verifyNoInteractions(gameService);
    }
}
