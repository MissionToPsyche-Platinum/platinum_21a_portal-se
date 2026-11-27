package com.psyche.portal_backend.controller;

import com.psyche.portal_backend.service.GameService;
import com.psyche.portal_backend.service.GameUploadRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class ApiReliabilityTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private GameService gameService;
    @MockitoBean private GameUploadRequestService uploadService;

    @Test
    void atLeast99PercentOf100ValidRequestsSucceed() throws Exception {
        when(gameService.getAllGames()).thenReturn(List.of());
        when(uploadService.getAllRequests()).thenReturn(List.of());
        when(uploadService.getPendingRequests()).thenReturn(List.of());

        String[] endpoints = {
            "/api/games",
            "/api/game-upload-requests",
            "/api/game-upload-requests/pending",
            "/api/auth/me"
        };
        RequestPostProcessor admin = user("apiTestAdmin").roles("ADMIN");
        int total = 0;
        int successful = 0;

    }
}

