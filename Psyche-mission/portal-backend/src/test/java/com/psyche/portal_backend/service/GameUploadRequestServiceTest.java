package com.psyche.portal_backend.service;

import tools.jackson.databind.json.JsonMapper;

import com.psyche.portal_backend.model.Game;
import com.psyche.portal_backend.model.GameUploadRequest;
import com.psyche.portal_backend.model.RequestStatus;
import com.psyche.portal_backend.model.User;
import com.psyche.portal_backend.model.dto.GameUploadRequestDTO;
import com.psyche.portal_backend.repository.UserRepository;
import com.psyche.portal_backend.repository.GameRepository;
import com.psyche.portal_backend.repository.GameUploadRequestRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameUploadRequestServiceTest {
    @Mock
    private GameUploadRequestRepository gameUploadRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameService gameService;

    @Mock
    private JsonMapper jsonMapper;

    private GameUploadRequestService gameUploadRequestService;

    /**
     * Initializes the GameUploadRequestService before each test.
     */
    @BeforeEach
    void setUp() {
        gameUploadRequestService = new GameUploadRequestService(gameUploadRequestRepository, userRepository, gameRepository, gameService, jsonMapper);
    }

    /**
     * Test 1: getRequestById(Long id)
     * Test case: Retrieve an existing request by ID
     * Input: ID = 1
     * Expected result: Returns matching request with ID = 1
     * Veriies that the repository's findById() method is called.
    */
    @Test
    void getRequestById_returnsRequestWhenFound() {
        Long requestId = 1L;
        GameUploadRequest req = mock(GameUploadRequest.class);

        when(gameUploadRequestRepository.findById(requestId)).thenReturn(Optional.of(req));

        GameUploadRequest result = gameUploadRequestService.getRequestById(requestId);

        assertNotNull(result);
        assertEquals(req, result);
        verify(gameUploadRequestRepository).findById(requestId);
    }

    /**
     * Test 2: getRequestById(Long id)
     * Test case: Retrieve non-existent request by ID
     * Input: ID = 18999, repository returns Optional.empty()
     * Expected result: Throws RuntimeException with "Request not found" message
     * Verifies that the repository is queried for the supplied ID.
     */
    @Test
    void getRequestById_throwsExceptionWhenNotFound() {
        Long requestId = 18999L;

        when(gameUploadRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> gameUploadRequestService.getRequestById(requestId));

        assertEquals("Request not found", exception.getMessage());
        verify(gameUploadRequestRepository).findById(requestId);
    }

    /**
     * Test 3: saveRequest(request)
     * Test case: Save an upload request to the repository.
     * Input: A mocked GameUploadRequest object
     * Expected result: Returns the same request object after it is saved.
     * Verifies that the repository's save() method is called with the request.
     */
    @Test
    void saveRequest_savesAndReturnsRequest() {
        GameUploadRequest request = mock(GameUploadRequest.class);

        when(gameUploadRequestRepository.save(request)).thenReturn(request);

        GameUploadRequest savedRequest = gameUploadRequestService.saveRequest(request);

        assertNotNull(savedRequest);
        assertEquals(request, savedRequest);
        verify(gameUploadRequestRepository).save(request);
    }

    /**
     * Test 4: getAllRequests()
     * Test case: Retrieve all upload requests from the repository.
     * Input: Repository returns a list containing two upload requests.
     * Expected result: Returns a list containng both requests.
     * Verifies the repostiory's findALl() method is called.
     */
    @Test
    void getAllRequests_returnsAllRequests() {
        List<GameUploadRequest> reqs = new ArrayList<>();

        GameUploadRequest req1 = mock(GameUploadRequest.class);
        GameUploadRequest req2 = mock(GameUploadRequest.class);

        reqs.add(req1);
        reqs.add(req2);

        when(gameUploadRequestRepository.findAll()).thenReturn(reqs);

        List<GameUploadRequest> results = gameUploadRequestService.getAllRequests();

        assertEquals(2, results.size());
        assertEquals(reqs, results);
        verify(gameUploadRequestRepository).findAll();
    }

    /**
     * Test 5: getAllRequests()
     * Test case: Retrieve all upload requests when none exist
     * Input: Repository returns an empty list
     * Expected result: Returns a non-null empty list.
     * Verifies that the repository's findAll() method is called.
     */
    @Test
    void getAllRequests_returnsEmptyListWithNoRequests() {
        when(gameUploadRequestRepository.findAll()).thenReturn(new ArrayList<>());

        List<GameUploadRequest> results = gameUploadRequestService.getAllRequests();

        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(gameUploadRequestRepository).findAll();
    }

    /**
     * Test 6: getRequestsByUsername(String username)
     * Test case: Retrieve upload requests submitted by a specific user.
     * Input: Username = "securitytest"; repository returns two requests associated
     * Expected result: Returns both matching upload requests.
     * Verifies that the repository is queried using the supplied username.
     */
    @Test
    void getRequestsByUsername_returnsRequestsForUsername() {
        String username = "securitytest";
        List<GameUploadRequest> reqs = new ArrayList<>();
        GameUploadRequest req1 = mock(GameUploadRequest.class);
        GameUploadRequest req2 = mock(GameUploadRequest.class);

        reqs.add(req1);
        reqs.add(req2);

        when(gameUploadRequestRepository.findBySubmittedBy_Username(username)).thenReturn(reqs);

        List<GameUploadRequest> results = gameUploadRequestService.getRequestsByUsername(username);

        assertEquals(2, results.size());
        assertEquals(reqs, results);
        verify(gameUploadRequestRepository).findBySubmittedBy_Username(username);
    }

    /**
     * Test 7: getRequestsByUsername(String username)
     * Test case: Retrieve upload requers for a username with no submissions.
     * Input: username = "fakeusername"; repository returns empty list
     * Expected result: Returns a non-null empty list.
     * Verifies that the repository is queried using the supplied username.
     */
    @Test
    void getRequestsByUsername_returnsEmptyListWithNoRequests() {
        String username = "fakeusername";

        when(gameUploadRequestRepository.findBySubmittedBy_Username(username)).thenReturn(new ArrayList<>());

        List<GameUploadRequest> results = gameUploadRequestService.getRequestsByUsername(username);

        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(gameUploadRequestRepository).findBySubmittedBy_Username(username);
    }

    /**
     * Test 8: getPendingRequests()
     * Test case: Retrieve all upload requests with PENDING status.
     * Input: Repository returns two requests with pending status
     * Expected result: Returns a list containing both pending requessts.
     * Verifies that the repository is queried with RequestStatus.PENDING.
     */
    @Test
    void getPendingRequests_returnsPendingRequests() {
        List<GameUploadRequest> reqs = new ArrayList<>();
        GameUploadRequest req1 = mock(GameUploadRequest.class);
        GameUploadRequest req2 = mock(GameUploadRequest.class);

        reqs.add(req1);
        reqs.add(req2);

        when(gameUploadRequestRepository.findByStatus(RequestStatus.PENDING)).thenReturn(reqs);

        List<GameUploadRequest> results = gameUploadRequestService.getPendingRequests();

        assertEquals(2, results.size());
        assertEquals(reqs, results);
        verify(gameUploadRequestRepository).findByStatus(RequestStatus.PENDING);
    }

    /**
     * Test 9: getPendingRequests()
     * Test case: Retrieve pending upload requests when none exist.
     * Input: Repository returns an empty list for PENDING status.
     * Expected result: Returns a non-null empty list.
     * Verifies that the repository is queried for pending requests.
     */
    @Test
    void getPendingRequests_returnsEmptyListWithNoPendingRequests() {
        when(gameUploadRequestRepository.findByStatus(RequestStatus.PENDING)).thenReturn(new ArrayList<>());

        List<GameUploadRequest> results = gameUploadRequestService.getPendingRequests();

        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(gameUploadRequestRepository).findByStatus(RequestStatus.PENDING);
    }

    /**
     * Test 10: denyRequest(Long id)
     * Test case: Deny an upload request that is currently pending.
     * Input: ID = 1; request has PENDING status.
     * Expected result: Changes the request status to DENIED and saves in repository.
     * Verifies that the repository's save() method is called.
     */
    @Test
    void denyRequest_changeStatusToDenied() {
        Long requestId = 1L;
        GameUploadRequest request = new GameUploadRequest();

        when(gameUploadRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
        when(gameUploadRequestRepository.save(request)).thenReturn(request);

        request.setRequestStatus(RequestStatus.PENDING);

        GameUploadRequest result = gameUploadRequestService.denyRequest(requestId);

        assertEquals(RequestStatus.DENIED, result.getRequestStatus());
        verify(gameUploadRequestRepository).save(request);
        assertEquals(request, result);
    }

    /**
     * Test 11: denyRequest(Long id)
     * Test case: Attempt to deny an upload request that does not exist.
     * Input: ID = 18999; repository returns Optional.empty()
     * Expected result: Throws a RuntimeException with "Request not found"
     * Verifies that the repository is queried for the supplied ID.
     */
    @Test
    void denyRequest_throwsExceptionWhenRequestNotFound() {
        Long requestId = 18999L;

        when(gameUploadRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> gameUploadRequestService.denyRequest(requestId));

        assertEquals("Request not found", exception.getMessage());
        verify(gameUploadRequestRepository).findById(requestId);
    }

    /**
     * Test 12: denyRequest(Long id)
     * Test case: Attempt to deny a request that is not pending.
     * Input: ID = 1; request has APPROVED status.
     * Expected result: Throws a RuntimeException with "Request is not pending"
     * Verifies that the repository is queried and the request is not saved.
     */
    @Test
    void denyRequest_throwsExceptionWhenNotPending() {
        Long requestId = 1L;
        GameUploadRequest req = new GameUploadRequest();

        req.setRequestStatus(RequestStatus.APPROVED);

        when(gameUploadRequestRepository.findById(requestId)).thenReturn(Optional.of(req));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> gameUploadRequestService.denyRequest(requestId));

        assertEquals("Request is not pending", exception.getMessage());
        verify(gameUploadRequestRepository).findById(requestId);
    }

    /**
     * Test 13: approveRequest(Long id)
     * Test case: Approve an upload request tat is currently pending and valid.
     * Input: ID = 1; request has PENDING status and is valid
     * Expected result: Saves the game, changes status to APPORVED
     * Verifies that both save operations are performed
     */
    @Test
    void approveRequest_approvePendingRequest() {
        Long requestId = 1L;
        Game game = mock(Game.class);
        GameUploadRequest req = new GameUploadRequest(game, null);

        when(gameUploadRequestRepository.findById(requestId)).thenReturn(Optional.of(req));
        when(gameUploadRequestRepository.save(req)).thenReturn(req);

        GameUploadRequest result = gameUploadRequestService.approveRequest(requestId);

        assertEquals(RequestStatus.APPROVED, result.getRequestStatus());
        assertEquals(game, result.getGame());
        verify(gameService).saveGame(game);
        verify(gameUploadRequestRepository).save(req);
    }

    /**
     * Test 14: approveRequest(Long id)
     * Test case: Attempt to approve an uplaod request that does not exist.
     * Input: ID = 18999; repository returns Optional.empty()
     * Expected result: Throws a RuntimeException with "Request not found"
     * Verifies that neither the game nor the request is saved
     */
    @Test
    void approveRequest_throwsExceptionWhenNotFound() {
        Long requestId = 18999L;

        when(gameUploadRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> gameUploadRequestService.approveRequest(requestId));

        assertEquals("Request not found", exception.getMessage());
        verify(gameService, never()).saveGame(any());
        verify(gameUploadRequestRepository, never()).save(any());
    }

    /**
     * Test 15: approveRequest(Long id)
     * Test case: Attempt to approve a request that has already been denied.
     * Input: ID = 1; request has DENIED status
     * Expected result: Throws a RuntimeException with the message "Request is not pending"
     * Verifies that neither the game nor the request is saved
     */
    @Test
    void approveRequest_throwsExceptionWhenNotPending() {
        Long requestId = 1L;
        GameUploadRequest req = new GameUploadRequest();

        req.setRequestStatus(RequestStatus.DENIED);

        when(gameUploadRequestRepository.findById(requestId)).thenReturn(Optional.of(req));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> gameUploadRequestService.approveRequest(requestId));

        assertEquals("Request is not pending", exception.getMessage());
        verify(gameService, never()).saveGame(any());
        verify(gameUploadRequestRepository, never()).save(any());
    }

    /**
     * Test 16: approveRequest(Long id)
     * Test case: Attempt to approve pending request with invalid game data.
     * Input: ID = 1; request is PENDING but invalid
     * Expected result: Throws a RuntimeException with the message "Request has no game data"
     * Verifies that neither the game nor the request is saved
     */
    @Test
    void approveRequest_throwsExceptionWhenNoGameDataExists() {
        Long requestId = 1L;
        GameUploadRequest req = new GameUploadRequest();

        req.setRequestStatus(RequestStatus.PENDING);

        when(gameUploadRequestRepository.findById(requestId)).thenReturn(Optional.of(req));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> gameUploadRequestService.approveRequest(requestId));

        assertEquals("Request has no game data", exception.getMessage());
        verify(gameService, never()).saveGame(any());
        verify(gameUploadRequestRepository, never()).save(any());
    }

    /**
     * Test 17: createRequest(dto, username)
     * Test case: Successfully create a new game uplaod request for an existing user.
     * Input: Username = "securitytest"; mocked User is returned by the user repository
     * Expected result: Returns a new GameUploadRequest object with PENDING status.
     * Verifies the user is looked up and new request is saved.
     */
    @Test
    void createRequest_createSuccessful() {
        String username = "securitytest";
        User user = mock(User.class);
        GameUploadRequestDTO dto = mock(GameUploadRequestDTO.class);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(jsonMapper.writeValueAsString(dto)).thenReturn("{}");
        when(gameUploadRequestRepository.save(any(GameUploadRequest.class))).thenAnswer(invocation -> {
            GameUploadRequest savedReq = invocation.getArgument(0);
            return savedReq;
        });

        GameUploadRequest result = gameUploadRequestService.createRequest(dto, username);

        assertNotNull(result);
        assertEquals(RequestStatus.PENDING, result.getRequestStatus());
        assertEquals(user, result.getSubmittedBy());
        assertEquals("{}", result.getGameJson());
        verify(userRepository).findByUsername(username);
        verify(gameUploadRequestRepository).save(any(GameUploadRequest.class));
    }

    @Test
    void createRequest_throwsExceptionWhenUserNotFound() {
        String username = "nonexistentuser";
        GameUploadRequestDTO dto = mock(GameUploadRequestDTO.class);

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> gameUploadRequestService.createRequest(dto, username));

        assertEquals("User not found", exception.getMessage());
        verify(gameUploadRequestRepository, never()).save(any(GameUploadRequest.class));
        verify(jsonMapper, never()).writeValueAsString(any());
    }
}
