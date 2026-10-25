package com.pictet.adventurebookapi.application.dto;

import com.pictet.adventurebookapi.domain.model.BookSectionType;
import lombok.Data;

@Data
public class ExplorationSectionSummaryDTO {
    private Integer id;
    private BookSectionType type;
}
