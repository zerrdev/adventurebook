package com.pictet.adventurebookapi.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pictet.adventurebookapi.model.Book;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class JsonParser {

    public Book parseJson(final String filePath) throws IOException {
        final var objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(filePath), Book.class);
    }
}
