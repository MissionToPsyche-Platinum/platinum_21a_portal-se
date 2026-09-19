package com.psyche.portal_backend.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.psyche.portal_backend.model.Game;
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

    public DBSeeder(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void run(String... args) throws Exception {
        try (InputStream inputStream = new ClassPathResource("games.json").getInputStream()) {
            List<Game> games = objectMapper.readValue(inputStream, new TypeReference<List<Game>>() {});
            gameRepository.saveAll(games);
        } catch(Exception e) {
            System.out.println("Failed to load the games.json file to the database");
        }
    }
    
}
