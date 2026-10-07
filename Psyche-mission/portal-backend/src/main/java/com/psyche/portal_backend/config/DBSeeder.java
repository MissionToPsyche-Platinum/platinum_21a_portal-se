package com.psyche.portal_backend.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.psyche.portal_backend.model.Game;
import com.psyche.portal_backend.model.User;
import com.psyche.portal_backend.repository.UserRepository;
import com.psyche.portal_backend.repository.GameRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

// fills the H2 database with the game data from the games.json file
@Component 
public class DBSeeder implements CommandLineRunner{

    private final GameRepository gameRepository;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    public DBSeeder(GameRepository gameRepository, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.objectMapper = new ObjectMapper();
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        try (InputStream inputStream = new ClassPathResource("games.json").getInputStream()) {
            List<Game> games = objectMapper.readValue(inputStream, new TypeReference<List<Game>>() {});
            gameRepository.saveAll(games);

            User test = new User("test", "test@gmail.com", "pwd");
            userRepository.save(test);
        } catch(Exception e) {
            System.out.println("Failed to load the games.json file to the database");
        }
    }
    
}
