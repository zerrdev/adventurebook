package com.pictet.adventurebookapi.application.dto;

import com.pictet.adventurebookapi.domain.model.OptionConsequenceType;
import lombok.Data;

@Data
public class BookSectionOptionConsequenceDTO {
    private OptionConsequenceType type;
    private Integer value;
    private String text;
}
