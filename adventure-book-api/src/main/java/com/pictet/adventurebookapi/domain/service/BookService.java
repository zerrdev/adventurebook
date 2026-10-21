package com.pictet.adventurebookapi.domain.service;

import com.pictet.adventurebookapi.domain.model.Book;
import com.pictet.adventurebookapi.domain.model.BookSection;
import com.pictet.adventurebookapi.domain.model.BookSectionOption;

import java.util.List;
import java.util.Optional;

public interface BookService {

    List<Book> getBooks();

    Optional<Book> findById(Integer bookId);

    BookSection getBookBeginSection(Book book);

    List<Integer> getUnreachableSections(Book book);
}
