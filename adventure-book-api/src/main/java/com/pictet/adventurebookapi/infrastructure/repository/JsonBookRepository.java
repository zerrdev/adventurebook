package com.pictet.adventurebookapi.infrastructure.repository;

import com.pictet.adventurebookapi.domain.model.Book;
import com.pictet.adventurebookapi.domain.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JsonBookRepository implements BookRepository {

    private final BookFileCatalog bookFileCatalog;

    @Override
    public List<Book> getBooks() {
        return bookFileCatalog.getBooks();
    }

    @Override
    public Optional<Book> getBookById(Integer id) {
        return Optional.ofNullable(bookFileCatalog.booksById().get(id));
    }
}
