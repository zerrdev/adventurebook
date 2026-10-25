package com.pictet.adventurebookapi.domain.service;

import com.pictet.adventurebookapi.domain.model.Book;
import com.pictet.adventurebookapi.domain.model.BookSection;
import com.pictet.adventurebookapi.domain.service.impl.ExplorationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExplorationServiceTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private ExplorationServiceImpl explorationService;

    @Test
    void ensureInvalidBooksCanBeExploredWithoutChangingSections() {
        final var section = new BookSection();
        section.setId(20);
        final var book = new Book();
        book.setErrors(List.of("Invalid target"));
        book.setSections(List.of(section));
        when(bookService.findById(3)).thenReturn(Optional.of(book));

        assertFalse(book.isValid());
        assertEquals(book.getSections(), explorationService.getSections(3));
        assertSame(section, explorationService.getSection(3, 20));
        assertEquals(List.of("Invalid target"), book.getErrors());
    }

    @Test
    void ensureMissingBookThrowsNotFound() {
        when(bookService.findById(99)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> explorationService.getSections(99));
        assertThrows(NoSuchElementException.class, () -> explorationService.getSection(99, 1));
    }

    @Test
    void ensureMissingSectionThrowsNotFound() {
        final var section = new BookSection();
        section.setId(1);
        final var book = new Book();
        book.setSections(List.of(section));
        when(bookService.findById(3)).thenReturn(Optional.of(book));

        assertThrows(NoSuchElementException.class, () -> explorationService.getSection(3, 50011));
    }

    @Test
    void ensureNullOrEmptySectionsReturnEmptyList() {
        final var book = new Book();
        when(bookService.findById(3)).thenReturn(Optional.of(book));

        assertTrue(explorationService.getSections(3).isEmpty());
        book.setSections(List.of());
        assertTrue(explorationService.getSections(3).isEmpty());
    }
}
