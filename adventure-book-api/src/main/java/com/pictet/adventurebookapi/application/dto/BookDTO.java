package com.pictet.adventurebookapi.application.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookDTO {
    private Integer id;
    private String title;
    private String author;
    private List<BookSectionDTO> sections;
}
