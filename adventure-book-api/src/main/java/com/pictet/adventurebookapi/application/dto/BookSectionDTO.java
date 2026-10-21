package com.pictet.adventurebookapi.application.dto;

import com.pictet.adventurebookapi.domain.model.BookSectionType;
import lombok.Data;

import java.util.List;

@Data
public class BookSectionDTO {
    private Integer id;
    private String text;
    private BookSectionType type;
    private List<BookSectionOptionDTO> options;
}
