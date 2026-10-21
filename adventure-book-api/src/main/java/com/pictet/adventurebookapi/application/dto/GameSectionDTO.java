package com.pictet.adventurebookapi.application.dto;

import com.pictet.adventurebookapi.domain.model.BookSectionType;
import lombok.Data;

@Data
public class GameSectionDTO {
    private String text;
    private BookSectionType type;
}
