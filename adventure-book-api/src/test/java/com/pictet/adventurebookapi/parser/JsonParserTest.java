package com.pictet.adventurebookapi.parser;

import com.pictet.adventurebookapi.infrastructure.parser.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JsonParserTest {

    private JsonParser jsonParser;

    @BeforeEach
    void setUp() {
        jsonParser = new JsonParser();
    }

    @Test
    void when_deserialize_the_prisoner__then_title_is_mapped() throws IOException {
        // GIVEN
        final var filePath = "src/main/resources/books/the-prisoner.json";

        // WHEN
        final var book = jsonParser.parseJson(filePath);

        // THEN
        assertNotNull(book);
        assertEquals("The Prisoner", book.getTitle());
    }
}
