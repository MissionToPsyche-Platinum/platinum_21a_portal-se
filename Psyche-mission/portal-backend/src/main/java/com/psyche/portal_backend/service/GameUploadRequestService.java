package com.psyche.portal_backend.service;

import java.util.List;
import com.psyche.portal_backend.model.User;
import com.psyche.portal_backend.model.Game;
import com.psyche.portal_backend.model.GameUploadRequest;
import com.psyche.portal_backend.model.RequestStatus;
import com.psyche.portal_backend.model.dto.GameUploadRequestDTO;
import com.psyche.portal_backend.repository.GameUploadRequestRepository;
import com.psyche.portal_backend.repository.UserRepository;
import com.psyche.portal_backend.repository.GameRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameUploadRequestService {

    private final GameUploadRequestRepository gameUploadRequestRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final GameService gameService;
    private final ObjectMapper objectMapper;

    public GameUploadRequestService(GameUploadRequestRepository gameUploadRequestRepository, UserRepository userRepository, GameRepository gameRepository, GameService gameService, ObjectMapper objectMapper) {
        this.gameUploadRequestRepository = gameUploadRequestRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.gameService = gameService;
        this.objectMapper = objectMapper;
    }

    public GameUploadRequest getRequestById(Long id) {
        Optional<GameUploadRequest> request = gameUploadRequestRepository.findById(id);

        if (request.isEmpty()) {
            throw new RuntimeException("Request not found");
        }

        return request.get();

    }

    public GameUploadRequest saveRequest(GameUploadRequest request) {
        return gameUploadRequestRepository.save(request);
    }

    public List<GameUploadRequest> getAllRequests() {
        return gameUploadRequestRepository.findAll();
    }



    public Optional<GameUploadRequest> getRequestByUsername(String username) {
        return gameUploadRequestRepository.findBySubmittedByUsername(username);
    }
    public GameUploadRequest createRequest(GameUploadRequestDTO dto) {
        Optional<User> user = userRepository.findByUsername("test");

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User actualUser = user.get();

        GameUploadRequest req = new GameUploadRequest(null, actualUser);
        try {
            req.setGameJson(objectMapper.writeValueAsString(dto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not save game upload request");
        }

        return gameUploadRequestRepository.save(req);

    }

    public List<GameUploadRequest> getPendingRequests() {
        return gameUploadRequestRepository.findByStatus(RequestStatus.PENDING);
    }

    public GameUploadRequest approveRequest(Long id) {
        GameUploadRequest request = getRequestById(id);

        if (request.getRequestStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Request is not pending");
        }

        Game game = request.getGame();
        if (game == null) {
            throw new RuntimeException("Request has no game data");
        }

        gameService.saveGame(game);
        request.setGame(game);
        request.setRequestStatus(RequestStatus.APPROVED);
        return gameUploadRequestRepository.save(request);
    }

    public GameUploadRequest denyRequest(Long id) {
        GameUploadRequest request = getRequestById(id);

        if (request.getRequestStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Request is not pending");
        }

        request.setRequestStatus(RequestStatus.DENIED);
        return gameUploadRequestRepository.save(request);
    }
}