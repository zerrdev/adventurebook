package com.pictet.adventurebookapi.infrastructure.repository;

import com.pictet.adventurebookapi.domain.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonBookRepositoryTest {

    private Book first;
    private Book second;
    private JsonBookRepository jsonBookRepository;

    @BeforeEach
    void setUp() {
        first = new Book();
        first.setId(1);
        second = new Book();
        second.setId(2);
        jsonBookRepository = new JsonBookRepository(new BookFileCatalog(List.of(first, second)));
    }

    @Test
    void ensureGetBooksReturnsAllBooks() {
        final var books = jsonBookRepository.getBooks();

        assertEquals(2, books.size());
        assertTrue(books.containsAll(List.of(first, second)));
    }

    @Test
    void ensureGetBookByIdFindsKnownBook() {
        final var book = jsonBookRepository.getBookById(2);

        assertTrue(book.isPresent());
        assertSame(second, book.get());
    }

    @Test
    void ensureGetBookByIdReturnsEmptyForUnknownId() {
        assertTrue(jsonBookRepository.getBookById(99).isEmpty());
    }

    @Test
    void ensureGetBookByIdReturnsEmptyForNullId() {
        final var book = assertDoesNotThrow(() -> jsonBookRepository.getBookById(null));

        assertTrue(book.isEmpty());
    }
}
