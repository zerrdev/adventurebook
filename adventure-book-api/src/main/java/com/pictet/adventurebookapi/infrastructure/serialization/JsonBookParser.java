package com.pictet.adventurebookapi.infrastructure.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pictet.adventurebookapi.domain.model.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonBookParser {

    private final ObjectMapper objectMapper;

    public Optional<Book> parseBookResource(final Resource resource) {
        try (InputStream inputStream = resource.getInputStream()) {
            return Optional.of(objectMapper.readValue(inputStream, Book.class));
        } catch (IOException e) {
            log.warn("Unable to load book file {}: {}", resource.getFilename(), e.getMessage());
            return Optional.empty();
        }
    }

}
