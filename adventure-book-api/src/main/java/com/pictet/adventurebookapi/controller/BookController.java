package com.pictet.adventurebookapi.controller;

import com.pictet.adventurebookapi.model.Book;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {

    @GetMapping
    public String getBooks() {
        // TODO implement actual logic to fetch and return books
        return  "List of books";
    }

    @GetMapping("/{bookId}")
    public Book getBookById(@PathVariable final Integer bookId) {
        // TODO implement actual logic to fetch and return a book by its ID
        return null;
    }
}
