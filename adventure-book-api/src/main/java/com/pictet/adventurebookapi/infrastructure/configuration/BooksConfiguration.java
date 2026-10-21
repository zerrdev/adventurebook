package com.pictet.adventurebookapi.infrastructure.configuration;

import com.pictet.adventurebookapi.domain.model.Book;
import com.pictet.adventurebookapi.domain.validators.BookValidation;
import com.pictet.adventurebookapi.infrastructure.repository.BookFileCatalog;
import com.pictet.adventurebookapi.infrastructure.serialization.JsonBookParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BooksConfiguration {

    @Value("${adventurebook.books-matcher}")
    private String booksMatcher;

    private final JsonBookParser jsonBookParser;

    private final List<BookValidation> bookValidations;

    @Bean(name="bookCatalog")
    public BookFileCatalog bookCatalog() throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:".concat(booksMatcher));
        final var bookList = Stream.of(resources)
                .map(jsonBookParser::parseBookResource)
                .flatMap(Optional::stream)
                .filter(this::isValid)
                .toList();

        return new BookFileCatalog(bookList);
    }

    private boolean isValid(Book book) {
        final var errors =bookValidations.stream()
                .flatMap(validation -> validation.validate(book).stream())
                .toList();

        if (!errors.isEmpty()) {
            log.warn("Unable to load '{}': {}", book.getTitle(), errors);
        }

        return errors.isEmpty();
    }

}
