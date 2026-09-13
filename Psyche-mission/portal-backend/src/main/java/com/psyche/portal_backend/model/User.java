package com.psyche.portal_backend.model;

import java.util.concurrent.atomic.AtomicInteger;

public class User {
    private static final AtomicInteger idGenerator = new AtomicInteger(1);
    private final int userId;
    private String username;
    private String email;
    private String password;

    public User(String username, String email, String password) {
        this.userId = idGenerator.getAndIncrement();
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getUsername() {
        return this.username;
    }
}