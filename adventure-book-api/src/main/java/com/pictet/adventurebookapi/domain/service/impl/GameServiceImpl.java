package com.pictet.adventurebookapi.domain.service.impl;

import com.pictet.adventurebookapi.domain.OptionConsequenceResolver;
import com.pictet.adventurebookapi.domain.model.*;
import com.pictet.adventurebookapi.domain.repository.GameRepository;
import com.pictet.adventurebookapi.domain.service.BookService;
import com.pictet.adventurebookapi.domain.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final BookService bookService;
    private final OptionConsequenceResolver optionConsequenceResolver;

    @Override
    public Game start(String playerId, Integer bookId) {

        Book book = bookService.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book doesn't exists", null));

        if (!book.isValid()) {
            throw new IllegalArgumentException("Book is invalid");
        }

        BookSection beginSection = bookService.getBookBeginSection(book);

        Game game = Game.builder()
                .playerId(playerId)
                .bookId(bookId)
                .healthPoints(10)
                .sectionId(beginSection.getId())
                .gameState(GameState.STARTED)
                .build();

        return gameRepository.save(game);
    }

    @Override
    public Optional<Game> findById(String gameId) {
        return gameRepository.findById(gameId);
    }

    @Override
    public BookSection getGameSection(String gameId) {
        Game game = getGame(gameId);
        Book book = getGameBook(game);

        return getGameSection(game, book);
    }

    @Override
    public List<BookSectionOption> getGameOptions(String gameId) {
        Game game = getGame(gameId);

        if (!GameState.STARTED.equals(game.getGameState())) {
            throw new IllegalStateException("Game has ended");
        }

        Book book = getGameBook(game);
        BookSection section = getGameSection(game, book);

        return Optional.ofNullable(section.getOptions()).orElse(List.of());
    }

    @Override
    public Game chooseOption(String gameId, Integer sectionId) {
        Game game = getGame(gameId);

        if (!GameState.STARTED.equals(game.getGameState())) {
            throw new IllegalStateException("Game has ended");
        }

        Book book = getGameBook(game);
        BookSection section = getGameSection(game, book);

        final var optionsMatching = Optional.ofNullable(section.getOptions()).orElse(List.of()).stream()
                .filter(o -> o.getGotoId().equals(sectionId)).findFirst().orElse(null);

        if (optionsMatching == null) {
            throw new IllegalArgumentException("You're not allowed to choose this option");
        }

        final var consequence = optionsMatching.getConsequence();
        game.setConsequenceText(consequence != null ? consequence.getText() : null);
        optionConsequenceResolver.apply(game, consequence);
        game.setSectionId(sectionId);
        checkGameEnd(game, book);
        return gameRepository.save(game);
    }

    private void checkGameEnd(Game game, Book book) {
        if (game.getHealthPoints() <= 0 || BookSectionType.END.equals(getGameSection(game, book).getType())) {
            game.setGameState(GameState.FINISHED);
        }
    }

    private Game getGame(String gameId) {
        return findById(gameId)
                .orElseThrow(() -> new NoSuchElementException("Game not found"));
    }

    private Book getGameBook(Game game) {
        return bookService.findById(game.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("Book not found for game"));
    }

    private BookSection getGameSection(Game game, Book book) {
        return book.getSections().stream().filter(section -> section.getId().equals(game.getSectionId())).findFirst()
                .orElseThrow(() -> new IllegalStateException("Section not found for game"));
    }
}
