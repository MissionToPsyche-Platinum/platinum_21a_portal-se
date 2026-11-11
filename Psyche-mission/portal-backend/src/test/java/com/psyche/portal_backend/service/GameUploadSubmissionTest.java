
package com.psyche.portal_backend.service;

import com.psyche.portal_backend.model.GameUploadRequest;
import com.psyche.portal_backend.model.RequestStatus;
import com.psyche.portal_backend.model.User;
import com.psyche.portal_backend.model.dto.GameUploadRequestDTO;
import com.psyche.portal_backend.repository.GameRepository;
import com.psyche.portal_backend.repository.GameUploadRequestRepository;
import com.psyche.portal_backend.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tools.jackson.databind.json.JsonMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameUploadSubmissionTest {

    @Mock
    private GameUploadRequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameService gameService;

    // TEST 1: Verify 10 valid submissions.
    @Test
    void tenValidRequestsAreSavedAsPendingWithTheirData() {

        User submitter = new User(
            "submissionTester",
            "submission@test.local",
            "test-hash"
        );

        when(userRepository.findByUsername("submissionTester"))
            .thenReturn(Optional.of(submitter));

        when(requestRepository.save(any(GameUploadRequest.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        GameUploadRequestService service =
            new GameUploadRequestService(
                requestRepository,
                userRepository,
                gameRepository,
                gameService,
                JsonMapper.builder().build()
            );

        for (int i = 1; i <= 10; i++) {

            GameUploadRequestDTO dto = validRequest(i);

            GameUploadRequest saved =
                service.createRequest(dto, "submissionTester");

            assertEquals(
                RequestStatus.PENDING,
                saved.getRequestStatus(),
                "Case " + i
            );

            assertSame(
                submitter,
                saved.getSubmittedBy(),
                "Case " + i
            );

            assertNotNull(saved.getGameJson());

            assertTrue(
                saved.getGameJson().contains(
                    "Sprint 12 Test Game " + i
                )
            );

            assertNotNull(saved.getGame());

            assertEquals(
                dto.getTitle(),
                saved.getGame().getTitle()
            );
        }

        verify(requestRepository, times(10))
            .save(any(GameUploadRequest.class));

        verifyNoInteractions(gameService);
    }

    // TEST 2: Reject a nonexistent user.
    @Test
    void unknownUsernameCannotCreateRequest() {

        when(userRepository.findByUsername("missing-user"))
            .thenReturn(Optional.empty());

        GameUploadRequestService service =
            new GameUploadRequestService(
                requestRepository,
                userRepository,
                gameRepository,
                gameService,
                JsonMapper.builder().build()
            );

        assertThrows(
            RuntimeException.class,
            () -> service.createRequest(
                validRequest(11),
                "missing-user"
            )
        );

        verify(requestRepository, never())
            .save(any(GameUploadRequest.class));
    }

    // Helper: Build valid test data.
    private static GameUploadRequestDTO validRequest(int index) {

        GameUploadRequestDTO dto =
            new GameUploadRequestDTO();

        dto.setTitle("Sprint 12 Test Game " + index);
        dto.setGenre("Educational");
        dto.setDifficulty("Easy");
        dto.setAge("10+");
        dto.setCredits("Sprint 12 QA");
        dto.setDescription("Test submission " + index);
        dto.setSrc("https://example.org/test-game-" + index);
        dto.setClassName("Science");
        dto.setGtype("Web");
        dto.setEngine("Browser");

        return dto;
    }
}
