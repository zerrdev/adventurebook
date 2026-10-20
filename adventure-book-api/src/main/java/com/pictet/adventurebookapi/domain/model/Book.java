package com.pictet.adventurebookapi.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
    private Integer id;
    private String title;
    private String author;
    private List<BookSection> sections;
}
