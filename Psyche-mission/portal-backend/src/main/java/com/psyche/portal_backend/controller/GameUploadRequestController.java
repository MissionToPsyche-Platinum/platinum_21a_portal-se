package com.psyche.portal_backend.controller;

import com.psyche.portal_backend.model.GameUploadRequest;
import com.psyche.portal_backend.model.dto.GameUploadRequestDTO;
import com.psyche.portal_backend.service.GameUploadRequestService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game-upload-requests")
@CrossOrigin(origins = "http://localhost:5173")
public class GameUploadRequestController {
    private final GameUploadRequestService service;

    public GameUploadRequestController(GameUploadRequestService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public GameUploadRequest getRequestById(@PathVariable Long id) {
        return service.getRequestById(id);
    }

    @PostMapping
    public GameUploadRequest createRequest(@RequestBody GameUploadRequestDTO dto) {
        return service.createRequest(dto);
    }
}