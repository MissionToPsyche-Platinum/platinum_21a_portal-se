package com.psyche.portal_backend.controller;

import com.psyche.portal_backend.model.GameUploadRequest;
import com.psyche.portal_backend.model.dto.GameUploadRequestDTO;
import com.psyche.portal_backend.service.GameUploadRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/game-upload-requests")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
public class GameUploadRequestController {
    private final GameUploadRequestService service;

    public GameUploadRequestController(GameUploadRequestService service) {
        this.service = service;
    }

    @GetMapping
    public List<GameUploadRequest> getAllRequests() {
        return service.getAllRequests();
    }

    @GetMapping("/pending")
    public List<GameUploadRequest> getPendingRequests(@RequestHeader(value = "X-Role", required = false) String role) {
        requireAdmin(role);
        return service.getPendingRequests();
    }

    @PutMapping("/{id}/approve")
    public GameUploadRequest approveRequest(
            @PathVariable Long id,
            @RequestHeader(value = "X-Role", required = false) String role) {
        requireAdmin(role);
        return service.approveRequest(id);
    }

    @PutMapping("/{id}/deny")
    public GameUploadRequest denyRequest(
            @PathVariable Long id,
            @RequestHeader(value = "X-Role", required = false) String role) {
        requireAdmin(role);
        return service.denyRequest(id);
    }

    @GetMapping("/{id}")
    public GameUploadRequest getRequestById(@PathVariable Long id) {
        return service.getRequestById(id);
    }

    @PostMapping
    public GameUploadRequest createRequest(@RequestBody GameUploadRequestDTO dto) {
        return service.createRequest(dto);
    }

    private void requireAdmin(String role) {
        if (!"admin".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin only");
        }
    }
}