package com.pictet.adventurebookapi.infrastructure.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pictet.adventurebookapi.domain.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonBookParserTest {

    private final JsonBookParser jsonBookParser = new JsonBookParser(new ObjectMapper());

    private final Resource positiveCaseResource = new ClassPathResource("books/the-prisoner.json");

    @Test
    void ensureBookParsingWorks() {
        final var book = jsonBookParser.parseBookResource(positiveCaseResource).orElseThrow();

        assertEquals(1, book.getId());
        assertEquals("The Prisoner", book.getTitle());
        assertEquals("Daniel El Fuego", book.getAuthor());
        assertNotNull(book.getSections());
        assertEquals(5, book.getSections().size());
    }

    @Test
    void ensureBookSectionParsingWorks() {
        final var book = jsonBookParser.parseBookResource(positiveCaseResource).orElseThrow();

        final var firstSection = book.getSections().getFirst();
        assertEquals(1, firstSection.getId());
        assertNotNull(firstSection.getText());
        assertEquals("BEGIN", firstSection.getType());
        assertEquals(2, firstSection.getOptions().size());
    }

    @Test
    void ensureBookSectionOptionsParsingWorks() {
        final var book = jsonBookParser.parseBookResource(positiveCaseResource).orElseThrow();

        final var firstOption = book.getSections().getFirst().getOptions().getFirst();
        assertEquals("You try to open the door", firstOption.getDescription());
        assertEquals(500, firstOption.getGotoId());
    }

    @Test
    void ensureStringSectionIdsAreConvertedToIntegers() {
        final var book = jsonBookParser.parseBookResource(positiveCaseResource).orElseThrow();

        final var sectionIds = book.getSections().stream().map(section -> section.getId()).toList();
        assertEquals(List.of(1, 20, 30, 500, 1000), sectionIds);
    }

    @Test
    void ensureUnknownFieldsAreIgnored() {
        final var book = jsonBookParser.parseBookResource(positiveCaseResource).orElseThrow();

        // section 20 has an option with a "consequence" field that the model does not map
        final var optionWithConsequence = book.getSections().get(1).getOptions().getFirst();
        assertEquals(30, optionWithConsequence.getGotoId());
    }

    @Test
    void ensureEndSectionWithoutOptionsIsParsed() {
        final var book = jsonBookParser.parseBookResource(positiveCaseResource).orElseThrow();

        final var lastSection = book.getSections().getLast();
        assertEquals("END", lastSection.getType());
        assertNull(lastSection.getOptions());
    }

    @Test
    void ensureNonFileResourceIsParsed() {
        final var json = """
                {"id": 7, "title": "In memory", "author": "Nobody", "sections": []}
                """;
        final var resource = new ByteArrayResource(json.getBytes(StandardCharsets.UTF_8));

        final var book = jsonBookParser.parseBookResource(resource).orElseThrow();

        assertEquals(7, book.getId());
        assertEquals("In memory", book.getTitle());
    }

    @Test
    void ensureInvalidJsonReturnsEmpty() {
        final var resource = new ByteArrayResource("{ not json".getBytes(StandardCharsets.UTF_8));

        final var book = assertDoesNotThrow(() -> jsonBookParser.parseBookResource(resource));

        assertTrue(book.isEmpty());
    }

    @Test
    void ensureMissingResourceReturnsEmpty() {
        final var resource = new ClassPathResource("books/does-not-exist.json");

        final var book = assertDoesNotThrow(() -> jsonBookParser.parseBookResource(resource));

        assertTrue(book.isEmpty());
    }

    @Test
    void ensureAllErrorsFixtureIsStillParsed() {
        // structural errors are a validation concern, not a parsing concern
        final var resource = new ClassPathResource("books/the-prisoner_all-errors.json");

        final var book = jsonBookParser.parseBookResource(resource);

        assertTrue(book.isPresent());
        assertEquals(2, book.map(Book::getId).orElseThrow());
    }
}
