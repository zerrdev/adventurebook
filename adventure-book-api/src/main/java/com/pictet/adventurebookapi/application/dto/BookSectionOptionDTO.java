package com.pictet.adventurebookapi.application.dto;

import lombok.Data;

@Data
public class BookSectionOptionDTO {
    private String description;
    private Integer gotoId;
    private BookSectionOptionConsequenceDTO consequence;
}
