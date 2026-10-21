package com.pictet.adventurebookapi.application.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookDetailsDTO {
    private Integer id;
    private String title;
    private String author;
    private boolean valid;
    private List<String> errors;
    private List<BookSectionDTO> sections;
    private List<Integer> unreachableSections;
}
