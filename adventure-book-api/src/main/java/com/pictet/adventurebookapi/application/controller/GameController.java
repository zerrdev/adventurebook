package com.pictet.adventurebookapi.application.controller;

import com.pictet.adventurebookapi.application.dto.ChooseOptionDTO;
import com.pictet.adventurebookapi.application.dto.GameDTO;
import com.pictet.adventurebookapi.application.dto.CreateGameDTO;
import com.pictet.adventurebookapi.application.dto.GameOptionsDTO;
import com.pictet.adventurebookapi.application.dto.GameSectionDTO;
import com.pictet.adventurebookapi.application.mapper.GameMapper;
import com.pictet.adventurebookapi.domain.service.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final GameMapper gameMapper;

    @PostMapping("/")
    public GameDTO newGame(@Valid @RequestBody final CreateGameDTO createGameDTO) {
        final var game = gameService.start(createGameDTO.getPlayerId(), createGameDTO.getBookId());

        return gameMapper.toDto(game);
    }

    @GetMapping("/{gameId}")
    public GameDTO getGame(@PathVariable final String gameId) {
        return gameService.findById(gameId)
                .map(gameMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
    }

    @GetMapping("/section/{gameId}")
    public GameSectionDTO getSection(@PathVariable final String gameId) {
        return gameMapper.toSectionDto(gameService.getGameSection(gameId));
    }

    @GetMapping("/options/{gameId}")
    public List<GameOptionsDTO> getOptions(@PathVariable final String gameId) {
        return gameMapper.toOptionsDto(gameService.getGameOptions(gameId));
    }

    @PostMapping("/choose/{gameId}")
    public GameDTO chooseOption(@PathVariable final String gameId, @Valid @RequestBody final ChooseOptionDTO chooseOptionDTO) {
        return gameMapper.toDto(gameService.chooseOption(gameId, chooseOptionDTO.getGotoId()));
    }

}
