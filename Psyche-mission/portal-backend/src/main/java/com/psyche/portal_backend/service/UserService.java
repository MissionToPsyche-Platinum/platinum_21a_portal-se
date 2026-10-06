package com.psyche.portal_backend.service;

import com.psyche.portal_backend.model.User;
import com.psyche.portal_backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        return user.get();
    }

    public User createUser(String username, String email, String password) {
        if (username == null || username.isBlank()
                || email == null || email.isBlank()
                || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Username, email, and password are required.");
        }

        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalStateException("Username already exists");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("Email already exists");
        }

        String hashedPassword = passwordEncoder.encode(password);
        return userRepository.save(new User(username, email, hashedPassword));
    }

    public User login(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Invalid credentials.");
        }

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty() || !passwordEncoder.matches(password, user.get().getPassword())) {
            throw new IllegalArgumentException("Invalid credentials.");
        }

        return user.get();
    }
}