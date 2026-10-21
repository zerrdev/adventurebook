package com.pictet.adventurebookapi.domain.repository;

import com.pictet.adventurebookapi.domain.model.Game;

import java.util.Optional;

public interface GameRepository {
    Game save(Game game);
    Optional<Game> findById(String gameId);
}
