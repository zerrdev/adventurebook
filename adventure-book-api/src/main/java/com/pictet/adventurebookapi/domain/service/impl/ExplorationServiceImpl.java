package com.pictet.adventurebookapi.domain.service.impl;

import com.pictet.adventurebookapi.domain.model.BookSection;
import com.pictet.adventurebookapi.domain.service.BookService;
import com.pictet.adventurebookapi.domain.service.ExplorationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExplorationServiceImpl implements ExplorationService {

    private final BookService bookService;

    @Override
    public List<BookSection> getSections(final Integer bookId) {
        final var book = bookService.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book not found"));
        return Optional.ofNullable(book.getSections()).orElse(List.of());
    }

    @Override
    public BookSection getSection(final Integer bookId, final Integer sectionId) {
        return getSections(bookId).stream()
                .filter(section -> sectionId.equals(section.getId())).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Node not found"));
    }
}
