package com.pictet.adventurebookapi.application.mapper;

import com.pictet.adventurebookapi.application.dto.GameDTO;
import com.pictet.adventurebookapi.application.dto.GameOptionsDTO;
import com.pictet.adventurebookapi.application.dto.GameSectionDTO;
import com.pictet.adventurebookapi.domain.model.BookSection;
import com.pictet.adventurebookapi.domain.model.BookSectionOption;
import com.pictet.adventurebookapi.domain.model.Game;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GameMapper {

    private final ModelMapper modelMapper;

    public GameDTO toDto(final Game game) {
        return modelMapper.map(game, GameDTO.class);
    }

    public GameSectionDTO toSectionDto(final BookSection section) {
        return modelMapper.map(section, GameSectionDTO.class);
    }

    public List<GameOptionsDTO> toOptionsDto(final List<BookSectionOption> options) {
        return options.stream().map(this::toOptionDto).toList();
    }

    public GameOptionsDTO toOptionDto(final BookSectionOption option) {
        return modelMapper.map(option, GameOptionsDTO.class);
    }

}
