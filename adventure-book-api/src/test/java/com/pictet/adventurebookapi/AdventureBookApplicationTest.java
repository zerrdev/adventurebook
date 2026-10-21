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
        assertEquals(1, bookCatalog.getBooks().size());
        assertNotNull(bookCatalog.booksById().get(1));
    }
}
