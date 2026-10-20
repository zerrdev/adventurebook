package com.pictet.adventurebookapi.infrastructure.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pictet.adventurebookapi.domain.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class BookParser {

    private final ObjectMapper objectMapper;

    public Book parseJsonResource(final Resource resource) throws IOException {
        return objectMapper.readValue(resource.getFile(), Book.class);
    }

}
