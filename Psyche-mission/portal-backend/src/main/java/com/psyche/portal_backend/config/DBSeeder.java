package com.psyche.portal_backend.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.psyche.portal_backend.model.Game;
import com.psyche.portal_backend.model.User;
import com.psyche.portal_backend.model.Role;
import com.psyche.portal_backend.repository.UserRepository;
import com.psyche.portal_backend.repository.GameRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.io.InputStream;
import java.util.List;

// Seeds PostgreSQL with game data and development test accounts
@Component 
public class DBSeeder implements CommandLineRunner{

    private final GameRepository gameRepository;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    public DBSeeder(GameRepository gameRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.gameRepository = gameRepository;
        this.objectMapper = new ObjectMapper();
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        try (InputStream inputStream = new ClassPathResource("games.json").getInputStream()) {
            List<Game> games = objectMapper.readValue(inputStream, new TypeReference<List<Game>>() {});
            gameRepository.saveAll(games);

            } catch (Exception e) {
                    System.err.println("Failed to seed games: " + e.getMessage());
                }

            if (userRepository.findByUsername("user").isEmpty()&& userRepository.findByEmail("test@gmail.com").isEmpty()) {
                User user = new User("user", "test@gmail.com", passwordEncoder.encode("pwd"));
                userRepository.save(user);
            }

            if (userRepository.findByUsername("admin").isEmpty()) {

             String adminEmail = "admin@gmail.com";


                if (userRepository.findByEmail(adminEmail).isPresent()) {
                            throw new IllegalStateException(
                                "Admin email is already registered."
                            );
                        }
                User admin = new User("admin", adminEmail, passwordEncoder.encode("test"), Role.ADMIN);
                userRepository.save(admin);
                System.out.println("Admin account initialized successfully.");

}
    }
    
}
