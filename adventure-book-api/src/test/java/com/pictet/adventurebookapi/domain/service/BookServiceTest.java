package com.pictet.adventurebookapi.domain.service;

import com.pictet.adventurebookapi.domain.model.Book;
import com.pictet.adventurebookapi.domain.repository.BookRepository;
import com.pictet.adventurebookapi.domain.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
