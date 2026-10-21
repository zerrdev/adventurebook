package com.pictet.adventurebookapi.infrastructure.repository;

import com.pictet.adventurebookapi.domain.model.Game;
import com.pictet.adventurebookapi.domain.repository.GameRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryGameRepository implements GameRepository {

    private final ConcurrentHashMap<String, Game> games = new ConcurrentHashMap<>();

    @Override
    public Game save(Game game) {
        if (game.getGameId() == null) {
            game.setGameId(UUID.randomUUID().toString());
        }
        games.put(game.getGameId(), game);
        return game;
    }

    @Override
    public Optional<Game> findById(String gameId) {
        return Optional.ofNullable(games.get(gameId));
    }

}
