package com.pictet.adventurebookapi.domain.service;

import com.pictet.adventurebookapi.domain.model.BookSection;

import java.util.List;

public interface ExplorationService {

    List<BookSection> getSections(Integer bookId);

    BookSection getSection(Integer bookId, Integer sectionId);
}
