package com.pictet.adventurebookapi.domain.repository;

import com.pictet.adventurebookapi.domain.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> getBooks();
    Optional<Book> getBookById(Integer id);
}
