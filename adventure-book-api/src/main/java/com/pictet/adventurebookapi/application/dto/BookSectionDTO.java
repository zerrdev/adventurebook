package com.pictet.adventurebookapi.application.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookSectionDTO {
    private Integer id;
    private String text;
    private String type;
    private List<BookSectionOptionDTO> options;
}
