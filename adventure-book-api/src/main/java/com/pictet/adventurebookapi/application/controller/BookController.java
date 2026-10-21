package com.pictet.adventurebookapi.application.controller;

import com.pictet.adventurebookapi.application.dto.BookDTO;
import com.pictet.adventurebookapi.application.dto.BookDetailsDTO;
import com.pictet.adventurebookapi.application.mapper.BookMapper;
import com.pictet.adventurebookapi.domain.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @GetMapping
    public List<BookDTO> getBooks() {
        return bookMapper.toDTO(bookService.getBooks());
    }

    @GetMapping("/{bookId}")
    public BookDetailsDTO getBookById(@PathVariable final Integer bookId) {
        return bookService.findById(bookId)
                .map(bookMapper::toDetailsDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
    }
}
