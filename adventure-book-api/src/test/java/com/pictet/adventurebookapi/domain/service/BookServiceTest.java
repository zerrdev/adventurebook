package com.pictet.adventurebookapi.domain.service;

import com.pictet.adventurebookapi.domain.model.Book;
import com.pictet.adventurebookapi.domain.model.BookSection;
import com.pictet.adventurebookapi.domain.model.BookSectionOption;
import com.pictet.adventurebookapi.domain.model.BookSectionType;
import com.pictet.adventurebookapi.domain.repository.BookRepository;
import com.pictet.adventurebookapi.domain.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void ensureGetBooksReturnsRepositoryBooks() {
        final var books = List.of(new Book(), new Book());
        when(bookRepository.getBooks()).thenReturn(books);

        final var result = bookService.getBooks();

        assertEquals(books, result);
        verify(bookRepository).getBooks();
    }

    @Test
    void ensureGetBooksReturnsEmptyListWhenRepositoryIsEmpty() {
        when(bookRepository.getBooks()).thenReturn(List.of());

        assertTrue(bookService.getBooks().isEmpty());
    }

    @Test
    void ensureFullyReachableBookHasNoUnreachableSections() {
        final var book = book(
                section(1, BookSectionType.BEGIN, 2, 3),
                section(2, BookSectionType.NODE, 3),
                section(3, BookSectionType.END));

        assertTrue(bookService.getUnreachableSections(book).isEmpty());
    }

    @Test
    void ensureSectionWithNoActionLeadingToItIsUnreachable() {
        final var book = book(
                section(1, BookSectionType.BEGIN, 3),
                section(2, BookSectionType.NODE, 3),
                section(3, BookSectionType.END));

        assertEquals(List.of(2), bookService.getUnreachableSections(book));
    }

    @Test
    void ensureSectionsOnlyReachableFromUnreachableSectionsAreUnreachable() {
        final var book = book(
                section(1, BookSectionType.BEGIN, 4),
                section(2, BookSectionType.NODE, 3),
                section(3, BookSectionType.NODE, 2),
                section(4, BookSectionType.END));

        assertEquals(List.of(2, 3), bookService.getUnreachableSections(book));
    }

    @Test
    void ensureLoopsAndUnknownGotoIdsDoNotBreakTheCheck() {
        final var book = book(
                section(1, BookSectionType.BEGIN, 2, 99),
                section(2, BookSectionType.NODE, 1, 3),
                section(3, BookSectionType.END));

        assertTrue(bookService.getUnreachableSections(book).isEmpty());
    }

    private static Book book(final BookSection... sections) {
        final var book = new Book();
        book.setSections(List.of(sections));
        return book;
    }

    private static BookSection section(final int id, final BookSectionType type, final Integer... gotoIds) {
        final var section = new BookSection();
        section.setId(id);
        section.setType(type);
        section.setOptions(Arrays.stream(gotoIds).map(gotoId -> {
            final var option = new BookSectionOption();
            option.setGotoId(gotoId);
            return option;
        }).toList());
        return section;
    }
}
