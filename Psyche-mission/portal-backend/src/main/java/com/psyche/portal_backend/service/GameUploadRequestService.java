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
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.core.JacksonException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameUploadRequestService {

    private final GameUploadRequestRepository gameUploadRequestRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final GameService gameService;
    private final JsonMapper jsonMapper;

    public GameUploadRequestService(GameUploadRequestRepository gameUploadRequestRepository, UserRepository userRepository, GameRepository gameRepository, GameService gameService, JsonMapper jsonMapper) {
        this.gameUploadRequestRepository = gameUploadRequestRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.gameService = gameService;
        this.jsonMapper = jsonMapper;
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

    public List<GameUploadRequest> getRequestsByUsername(String username) {
        return gameUploadRequestRepository.findBySubmittedBy_Username(username);
    }

    public GameUploadRequest createRequest(GameUploadRequestDTO dto, String username) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User actualUser = user.get();

        GameUploadRequest req = new GameUploadRequest(null, actualUser);
        try {
            req.setGameJson(jsonMapper.writeValueAsString(dto));
        } catch (JacksonException e) {
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