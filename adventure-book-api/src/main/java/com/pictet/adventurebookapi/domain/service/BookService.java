package com.pictet.adventurebookapi.domain.service;

import com.pictet.adventurebookapi.domain.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    List<Book> getBooks();

    Optional<Book> findById(Integer bookId);
}
