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
        for (String endpoint : endpoints) {
            int endpointPassed = 0;
            for (int i = 0; i < 25; i++) {
                int status = mockMvc.perform(get(endpoint).with(admin))
                    .andReturn().getResponse().getStatus();
                total++;
                if (status == 200) {
                    successful++;
                    endpointPassed++;
                } else {
                    System.out.printf("FAIL %s request %d: HTTP %d%n", endpoint, i + 1, status);
                }
            }
            System.out.printf("%s: %d/25 successful%n", endpoint, endpointPassed);
        }
        double successRate = 100.0 * successful / total;
        System.out.printf("API VALID-REQUEST RESULT: %d/%d = %.2f%%%n", successful, total, successRate);
        assertTrue(successRate >= 99.0,
            String.format("Expected >=99%% but measured %.2f%%", successRate));
    }
}

