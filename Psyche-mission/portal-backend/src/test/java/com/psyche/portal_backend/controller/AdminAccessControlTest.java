
package com.psyche.portal_backend.controller;

import com.psyche.portal_backend.service.GameUploadRequestService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@AutoConfigureMockMvc
class AdminAccessControlTest {

    private static final long REQUEST_ID = 99999L;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameUploadRequestService service;

    //administrator approve requests
    @Test
    void adminCanApproveRequest() throws Exception {

        mockMvc.perform(
            put("/api/game-upload-requests/{id}/approve", REQUEST_ID)
                .with(user("admin").roles("ADMIN"))
                .with(csrf())
        ).andExpect(status().isOk());

        verify(service).approveRequest(REQUEST_ID);
    }

    //administrator deny requests
    @Test
    void adminCanDenyRequest() throws Exception {

        mockMvc.perform(
            put("/api/game-upload-requests/{id}/deny", REQUEST_ID)
                .with(user("admin").roles("ADMIN"))
                .with(csrf())
        ).andExpect(status().isOk());

        verify(service).denyRequest(REQUEST_ID);
    }

    //regular user 1 cannot approve requests
   @Test
   void regularUserCannotApproveRequest() throws Exception {

       mockMvc.perform(
           put("/api/game-upload-requests/{id}/approve", REQUEST_ID)
               .with(user("regularUser1").roles("USER"))
               .with(csrf())
       ).andExpect(status().isForbidden());

       verify(service, never()).approveRequest(anyLong());
   }

    //regular user 2 cannot deny requests
    @Test
    void secondRegularUserCannotDenyRequest() throws Exception {

        mockMvc.perform(
            put("/api/game-upload-requests/{id}/deny", REQUEST_ID)
                .with(user("regularUser2").roles("USER"))
                .with(csrf())
        ).andExpect(status().isForbidden());

        verify(service, never()).denyRequest(anyLong());
    }

    //unauthenticated user cannot approve requests
    @Test
    void unauthenticatedUserCannotApproveRequest() throws Exception {

        mockMvc.perform(
            put("/api/game-upload-requests/{id}/approve", REQUEST_ID)
                .with(csrf())
        ).andExpect(status().isUnauthorized());

        verify(service, never()).approveRequest(anyLong());
    }

    //administrator can access pending requests
    @Test
    void adminCanViewPendingRequests() throws Exception {

        when(service.getPendingRequests()).thenReturn(List.of());

        mockMvc.perform(
            get("/api/game-upload-requests/pending")
                .with(user("admin").roles("ADMIN"))
        ).andExpect(status().isOk());

        verify(service).getPendingRequests();
    }



    //regular user 1 cannot deny requests
    @Test
    void regularUserCannotDenyRequest() throws Exception {

        mockMvc.perform(
            put("/api/game-upload-requests/{id}/deny", REQUEST_ID)
                .with(user("regularUser1").roles("USER"))
                .with(csrf())
        ).andExpect(status().isForbidden());

        verify(service, never()).denyRequest(anyLong());
    }

    //regular user 2 cannot approve requests
    @Test
    void secondRegularUserCannotApproveRequest() throws Exception {

        mockMvc.perform(
            put("/api/game-upload-requests/{id}/approve", REQUEST_ID)
                .with(user("regularUser2").roles("USER"))
                .with(csrf())
        ).andExpect(status().isForbidden());

        verify(service, never()).approveRequest(anyLong());
    }

    //regular users cannot view pending requests
    @Test
    void regularUsersCannotViewPendingRequests() throws Exception {

        mockMvc.perform(
            get("/api/game-upload-requests/pending")
                .with(user("regularUser1").roles("USER"))
        ).andExpect(status().isForbidden());

        mockMvc.perform(
            get("/api/game-upload-requests/pending")
                .with(user("regularUser2").roles("USER"))
        ).andExpect(status().isForbidden());

        verify(service, never()).getPendingRequests();
    }

    //unauthenticated users cannot view pending requests
    @Test
    void unauthenticatedUserCannotViewPendingRequests()
            throws Exception {

        mockMvc.perform(
            get("/api/game-upload-requests/pending")
        ).andExpect(status().isUnauthorized());

        verify(service, never()).getPendingRequests();
    }

}
