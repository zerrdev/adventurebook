package com.pictet.adventurebookapi;

import com.pictet.adventurebookapi.infrastructure.repository.BookFileCatalog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdventureBookApplicationTest {

    @Autowired
    private BookFileCatalog bookCatalog;

    @Test
    void ensureBooksAreLoadedFromClasspath() {
        assertEquals(3, bookCatalog.getBooks().size());
        assertNotNull(bookCatalog.booksById().get(1));
    }

    @Test
    void ensureInvalidBooksAreLoadedWithErrors() {
        assertTrue(bookCatalog.booksById().get(1).isValid());
        assertTrue(bookCatalog.booksById().get(1).getErrors().isEmpty());

        assertFalse(bookCatalog.booksById().get(2).isValid());
        assertFalse(bookCatalog.booksById().get(3).isValid());
        assertTrue(bookCatalog.booksById().get(3).getErrors().contains("Invalid section gotoId: 50011"));
    }
}
