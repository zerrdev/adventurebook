package com.pictet.adventurebookapi.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookSection {
    private Integer id;
    private String text;
    private BookSectionType type;
    private List<BookSectionOption> options;
}
