package com.psyche.portal_backend.model.dto;

public class UpdateProfileRequest {
    private String username;
    private String email;

    public UpdateProfileRequest() { }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
