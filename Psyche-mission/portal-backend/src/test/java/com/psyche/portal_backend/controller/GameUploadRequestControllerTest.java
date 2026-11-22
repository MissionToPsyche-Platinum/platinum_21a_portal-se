package com.psyche.portal_backend.controller;

import com.psyche.portal_backend.model.GameUploadRequest;
import com.psyche.portal_backend.model.RequestStatus;
import com.psyche.portal_backend.model.dto.GameUploadRequestDTO;
import com.psyche.portal_backend.service.GameUploadRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameUploadRequestController.class)
public class GameUploadRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameUploadRequestService service;

    @Test
    void shouldGetAllRequests() throws Exception {
        GameUploadRequest req1 = new GameUploadRequest();
        GameUploadRequest req2 = new GameUploadRequest();
        List<GameUploadRequest> requests = new ArrayList<>();

        req1.setRequestStatus(RequestStatus.PENDING);
        req2.setRequestStatus(RequestStatus.DENIED);

        requests.add(req1);
        requests.add(req2);

        when(service.getAllRequests()).thenReturn(requests);

        mockMvc.perform(get("/api/game-upload-requests")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2));

        verify(service).getAllRequests();
    }

    @Test
    void shouldGetPendingRequests() throws Exception {
        GameUploadRequest req1 = new GameUploadRequest();
        GameUploadRequest req2 = new GameUploadRequest();
        List<GameUploadRequest> requests = new ArrayList<>();

        req1.setRequestStatus(RequestStatus.PENDING);
        req2.setRequestStatus(RequestStatus.APPROVED);

        requests.add(req1);

        when(service.getPendingRequests()).thenReturn(requests);

        mockMvc.perform(get("/api/game-upload-requests/pending")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));

        verify(service).getPendingRequests();
    }

    @Test
    void shouldApproveRequest() throws Exception {
        GameUploadRequest req = new GameUploadRequest();
        Long requestId = 1L;

        req.setRequestStatus(RequestStatus.APPROVED);

        when(service.approveRequest(requestId)).thenReturn(req);

        mockMvc.perform(put("/api/game-upload-requests/" + requestId + "/approve")).andExpect(status().isOk()).andExpect(jsonPath("$.requestStatus").value("APPROVED"));

        verify(service).approveRequest(requestId);
    }

    @Test
    void shouldDenyRequest() throws Exception {
        GameUploadRequest req = new GameUploadRequest();
        Long requestId = 1L;

        req.setRequestStatus(RequestStatus.DENIED);

        when(service.denyRequest(requestId)).thenReturn(req);

        mockMvc.perform(put("/api/game-upload-requests/" + requestId + "/deny")).andExpect(status().isOk()).andExpect(jsonPath("$.requestStatus").value("DENIED"));

        verify(service).denyRequest(requestId);
    }

    @Test
    void shouldGetRequestById() throws Exception {
        GameUploadRequest req = new GameUploadRequest();
        Long requestId = 1L;

        req.setRequestStatus(RequestStatus.PENDING);

        when(service.getRequestById(requestId)).thenReturn(req);

        mockMvc.perform(get("/api/game-upload-requests/" + requestId)).andExpect(status().isOk()).andExpect(jsonPath("$.requestStatus").value("PENDING"));

        verify(service).getRequestById(requestId);
    }

    @Test
    void shouldGetMyRequests() throws Exception {
        GameUploadRequest req1 = new GameUploadRequest();
        GameUploadRequest req2 = new GameUploadRequest();
        List<GameUploadRequest> requests = new ArrayList<>();

        req1.setRequestStatus(RequestStatus.PENDING);
        req2.setRequestStatus(RequestStatus.DENIED);

        requests.add(req1);
        requests.add(req2);

        when(service.getRequestsByUsername("testuser")).thenReturn(requests);

        mockMvc.perform(get("/api/game-upload-requests/requests").header("X-Username", "testuser")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2));

        verify(service).getRequestsByUsername("testuser");
    }

    @Test
    void shouldCreateRequest() throws Exception {
        GameUploadRequest req = new GameUploadRequest();

        req.setRequestStatus(RequestStatus.PENDING);

        when(service.createRequest(any(GameUploadRequestDTO.class), eq("testuser"))).thenReturn(req);

        String requestJson = """
               {
                    "title": "Test Game",
                    "thumbnail": "https://example.com/thumbnail.jpg",
                    "genre": "Action",
                    "difficulty": "Easy",
                    "age": "Middle School",
                    "className": "Platinum",
                    "credits": "1",
                    "gtype": "platinum",
                    "engine": "Unity",
                    "description": "A test game for testing purposes.",
                    "src": "https://example.com/game.zip",
                    "video": "https://example.com/video.mp4"
               }
               """;

        mockMvc.perform(post("/api/game-upload-requests").header("X-Username", "testuser").contentType(MediaType.APPLICATION_JSON).content(requestJson)).andExpect(status().isOk()).andExpect(jsonPath("$.requestStatus").value("PENDING"));

        verify(service).createRequest(any(GameUploadRequestDTO.class), eq("testuser"));
    }

    @Test
    void shouldReturnBadRequestWhenUsernameHeaderIsNotPresent() throws Exception {
        mockMvc.perform(get("/api/game-upload-requests/requests")).andExpect(status().isBadRequest());
        verify(service, never()).getRequestsByUsername(anyString());
    }

    @Test
    void shouldReturnEmptyListWhenNoRequestsFound() throws Exception {
        when(service.getAllRequests()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/api/game-upload-requests/requests").header("X-Username", "testuser")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));
        verify(service).getRequestsByUsername("testuser");
    }
}
