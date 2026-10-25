package com.pictet.adventurebookapi.application.dto;

import com.pictet.adventurebookapi.domain.model.BookSectionType;
import lombok.Data;

import java.util.List;

@Data
public class ExplorationSectionDTO {
    private Integer id;
    private String text;
    private BookSectionType type;
    private List<GameOptionsDTO> options;
}
