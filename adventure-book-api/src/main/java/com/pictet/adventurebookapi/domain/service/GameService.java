package com.pictet.adventurebookapi.domain.service;

import com.pictet.adventurebookapi.domain.model.BookSection;
import com.pictet.adventurebookapi.domain.model.BookSectionOption;
import com.pictet.adventurebookapi.domain.model.Game;

import java.util.List;
import java.util.Optional;

public interface GameService {
    Game start(String playerId, Integer bookId);
    Optional<Game> findById(String gameId);
    BookSection getGameSection(String gameId);
    List<BookSectionOption> getGameOptions(String gameId);
    Game chooseOption(String gameId, Integer optionId);
}
