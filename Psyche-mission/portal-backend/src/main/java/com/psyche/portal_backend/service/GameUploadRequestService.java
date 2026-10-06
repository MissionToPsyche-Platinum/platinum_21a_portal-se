package com.psyche.portal_backend.service;

import java.util.List;
import com.psyche.portal_backend.model.User;
import com.psyche.portal_backend.model.Game;
import com.psyche.portal_backend.model.GameUploadRequest;
import com.psyche.portal_backend.model.dto.GameUploadRequestDTO;
import com.psyche.portal_backend.repository.GameUploadRequestRepository;
import com.psyche.portal_backend.repository.UserRepository;
import com.psyche.portal_backend.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameUploadRequestService {

    private final GameUploadRequestRepository gameUploadRequestRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    public GameUploadRequestService(GameUploadRequestRepository gameUploadRequestRepository, UserRepository userRepository, GameRepository gameRepository) {
        this.gameUploadRequestRepository = gameUploadRequestRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
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
        Game game = new Game(dto.getTitle(), dto.getGenre(), dto.getDifficulty(),
                dto.getAge(), dto.getClassName(), dto.getCredits(), dto.getGtype(),
                dto.getEngine(), dto.getDescription(), dto.getThumbnail(), dto.getSrc(),
                dto.getVideo());

        gameRepository.save(game);

        Optional<User> user = userRepository.findByUsername("test");

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User actualUser = user.get();

        GameUploadRequest req = new GameUploadRequest(game, actualUser);

        return gameUploadRequestRepository.save(req);

    }
}