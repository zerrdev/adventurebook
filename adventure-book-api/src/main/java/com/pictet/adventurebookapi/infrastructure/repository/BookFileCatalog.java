package com.pictet.adventurebookapi.infrastructure.repository;

import com.pictet.adventurebookapi.domain.model.Book;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public record BookFileCatalog(Map<Integer, Book> booksById) {

    public BookFileCatalog(Collection<Book> bookList) {
        this(indexById(bookList));
    }

    public List<Book> getBooks() {
        return new ArrayList<>(booksById.values());
    }

    private static Map<Integer, Book> indexById(final Collection<Book> bookList) {
        final Map<Integer, Book> booksById = new HashMap<>();
        for (final Book book : bookList) {
            final Book existing = booksById.putIfAbsent(book.getId(), book);
            if (existing != null) {
                log.warn("Skipping '{}': A book with id {} already exists", book.getTitle(), book.getId());
            }
        }
        return booksById;
    }

}
