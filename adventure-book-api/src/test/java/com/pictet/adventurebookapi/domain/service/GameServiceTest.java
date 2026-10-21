package com.pictet.adventurebookapi.domain.service;

import com.pictet.adventurebookapi.domain.OptionConsequenceResolver;
import com.pictet.adventurebookapi.domain.handlers.impl.LoseHealthConsequenceHandler;
import com.pictet.adventurebookapi.domain.model.*;
import com.pictet.adventurebookapi.domain.repository.GameRepository;
import com.pictet.adventurebookapi.domain.service.impl.GameServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    private static final String GAME_ID = "game-1";

    @Mock
    private GameRepository gameRepository;

    @Mock
    private BookService bookService;

    private GameServiceImpl gameService;

    private Book book;

    @BeforeEach
    void setUp() {
        gameService = new GameServiceImpl(gameRepository, bookService,
                new OptionConsequenceResolver(List.of(new LoseHealthConsequenceHandler())));

        book = new Book();
        book.setId(1);
        book.setSections(List.of(
                section(1, BookSectionType.BEGIN, List.of(option(20, null), option(30, loseHealth(6)))),
                section(20, BookSectionType.NODE, List.of(option(1000, loseHealth(20)))),
                section(30, BookSectionType.NODE, List.of(option(1000, null))),
                section(1000, BookSectionType.END, null)));

        lenient().when(bookService.findById(1)).thenReturn(Optional.of(book));
        lenient().when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void ensureStartCreatesGameOnBeginSection() {
        when(bookService.getBookBeginSection(book)).thenReturn(book.getSections().get(0));

        final var game = gameService.start("player", 1);

        assertEquals(1, game.getSectionId());
        assertEquals(10, game.getHealthPoints());
        assertEquals(GameState.STARTED, game.getGameState());
    }

    @Test
    void ensureStartFailsWhenBookDoesNotExist() {
        when(bookService.findById(99)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> gameService.start("player", 99));
    }

    @Test
    void ensureStartFailsWhenBookIsInvalid() {
        book.setErrors(List.of("A book cannot have less than one end"));

        assertThrows(IllegalArgumentException.class, () -> gameService.start("player", 1));
    }

    @Test
    void ensureGetGameSectionReturnsCurrentSection() {
        givenGame(30, 10, GameState.STARTED);

        assertEquals(30, gameService.getGameSection(GAME_ID).getId());
    }

    @Test
    void ensureGetGameOptionsReturnsCurrentSectionOptions() {
        givenGame(1, 10, GameState.STARTED);

        final var options = gameService.getGameOptions(GAME_ID);

        assertEquals(List.of(20, 30), options.stream().map(BookSectionOption::getGotoId).toList());
    }

    @Test
    void ensureGetGameOptionsFailsWhenGameNotFound() {
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> gameService.getGameOptions(GAME_ID));
    }

    @Test
    void ensureGetGameOptionsFailsWhenGameFinished() {
        givenGame(1000, 10, GameState.FINISHED);

        assertThrows(IllegalStateException.class, () -> gameService.getGameOptions(GAME_ID));
    }

    @Test
    void ensureChooseOptionMovesToSectionAndAppliesConsequence() {
        givenGame(1, 10, GameState.STARTED);

        final var game = gameService.chooseOption(GAME_ID, 30);

        assertEquals(30, game.getSectionId());
        assertEquals(4, game.getHealthPoints());
        assertEquals("Lose 6", game.getConsequenceText());
        assertEquals(GameState.STARTED, game.getGameState());
    }

    @Test
    void ensureChooseOptionClearsConsequenceTextWhenOptionHasNoConsequence() {
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.of(
                Game.builder().gameId(GAME_ID).bookId(1).sectionId(30).healthPoints(4)
                        .gameState(GameState.STARTED).consequenceText("Lose 6").build()));

        final var game = gameService.chooseOption(GAME_ID, 1000);

        assertNull(game.getConsequenceText());
    }

    @Test
    void ensureChooseOptionFinishesGameOnEndSection() {
        givenGame(30, 10, GameState.STARTED);

        final var game = gameService.chooseOption(GAME_ID, 1000);

        assertEquals(1000, game.getSectionId());
        assertEquals(GameState.FINISHED, game.getGameState());
    }

    @Test
    void ensureChooseOptionFinishesGameWhenHealthReachesZero() {
        givenGame(20, 10, GameState.STARTED);

        final var game = gameService.chooseOption(GAME_ID, 1000);

        assertEquals(0, game.getHealthPoints());
        assertEquals(GameState.FINISHED, game.getGameState());
    }

    @Test
    void ensureChooseOptionFailsForOptionNotInCurrentSection() {
        givenGame(1, 10, GameState.STARTED);

        assertThrows(IllegalArgumentException.class, () -> gameService.chooseOption(GAME_ID, 1000));
    }

    @Test
    void ensureChooseOptionFailsWhenGameFinished() {
        givenGame(1000, 10, GameState.FINISHED);

        assertThrows(IllegalStateException.class, () -> gameService.chooseOption(GAME_ID, 1));
    }

    private void givenGame(Integer sectionId, int healthPoints, GameState state) {
        final var game = Game.builder()
                .gameId(GAME_ID)
                .playerId("player")
                .bookId(1)
                .sectionId(sectionId)
                .healthPoints(healthPoints)
                .gameState(state)
                .build();
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.of(game));
    }

    private static BookSection section(Integer id, BookSectionType type, List<BookSectionOption> options) {
        final var section = new BookSection();
        section.setId(id);
        section.setText("Section " + id);
        section.setType(type);
        section.setOptions(options);
        return section;
    }

    private static BookSectionOption option(Integer gotoId, BookSectionOptionConsequence consequence) {
        final var option = new BookSectionOption();
        option.setDescription("Go to " + gotoId);
        option.setGotoId(gotoId);
        option.setConsequence(consequence);
        return option;
    }

    private static BookSectionOptionConsequence loseHealth(Integer value) {
        final var consequence = new BookSectionOptionConsequence();
        consequence.setType(OptionConsequenceType.LOSE_HEALTH);
        consequence.setValue(value);
        consequence.setText("Lose " + value);
        return consequence;
    }
}
