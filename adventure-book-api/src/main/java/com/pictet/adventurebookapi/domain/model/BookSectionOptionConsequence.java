package com.pictet.adventurebookapi.domain.model;

import lombok.Data;

@Data
public class BookSectionOptionConsequence {
    private OptionConsequenceType type;
    private Integer value;
    private String text;
}
