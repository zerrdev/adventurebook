package com.pictet.adventurebookapi.infrastructure.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pictet.adventurebookapi.domain.model.Book;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class JsonParser {

    public Book parseJson(final String filePath) throws IOException {
        return objectMapper.readValue(new File(filePath), Book.class);
    }
}
