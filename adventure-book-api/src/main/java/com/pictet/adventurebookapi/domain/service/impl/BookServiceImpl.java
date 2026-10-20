package com.pictet.adventurebookapi.domain.service.impl;

import com.pictet.adventurebookapi.domain.model.Book;
import com.pictet.adventurebookapi.domain.repository.BookRepository;
import com.pictet.adventurebookapi.domain.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public List<Book> getBooks() {
        return bookRepository.getBooks();
    }
}
