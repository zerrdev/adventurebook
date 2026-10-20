package com.pictet.adventurebookapi.infrastructure.repository;

import com.pictet.adventurebookapi.domain.model.Book;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookFileCatalogTest {

    private static Book book(final Integer id, final String title) {
        final var book = new Book();
        book.setId(id);
        book.setTitle(title);
        return book;
    }

    @Test
    void ensureAllBooksAreReturned() {
        final var first = book(1, "First");
        final var second = book(2, "Second");

        final var catalog = new BookFileCatalog(List.of(first, second));

        assertEquals(2, catalog.getBooks().size());
        assertTrue(catalog.getBooks().containsAll(List.of(first, second)));
    }

    @Test
    void ensureBooksAreIndexedById() {
        final var first = book(1, "First");
        final var second = book(2, "Second");

        final var catalog = new BookFileCatalog(List.of(first, second));

        assertSame(first, catalog.booksById().get(1));
        assertSame(second, catalog.booksById().get(2));
    }

    @Test
    void ensureEmptyCollectionGivesEmptyCatalog() {
        final var catalog = new BookFileCatalog(List.of());

        assertTrue(catalog.getBooks().isEmpty());
        assertTrue(catalog.booksById().isEmpty());
    }

    @Test
    void ensureDuplicateIdIsSkipped() {
        final var first = book(1, "First");
        final var duplicate = book(1, "Duplicate");
        final var second = book(2, "Second");

        final var catalog = assertDoesNotThrow(() -> new BookFileCatalog(List.of(first, duplicate, second)));

        assertEquals(2, catalog.getBooks().size());
        assertSame(first, catalog.booksById().get(1));
        assertFalse(catalog.getBooks().contains(duplicate));
    }

    @Test
    void ensureGetBooksReturnsACopy() {
        final var catalog = new BookFileCatalog(List.of(book(1, "First")));

        catalog.getBooks().clear();

        assertEquals(1, catalog.getBooks().size());
    }
}
