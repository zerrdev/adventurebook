package com.pictet.adventurebookapi.infrastructure.serialization;

import com.pictet.adventurebookapi.domain.model.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookParserTest {

    @Autowired
    private BookParser bookParser;

    private static Collection<Resource> booksResources;

    private final String positiveCaseFilename = "the-prisoner.json";

    @BeforeAll
    static void beforeAll() throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath*:/books/*.json");
        booksResources = List.of(resources);
    }

    @Test
    void ensureResourcesLoaded() throws IOException {
        assertNotNull(booksResources);
        assertNotEquals(0, booksResources.size());
    }

    @Test
    void ensurePositiveCaseFileExists() {
        final var fileResource = booksResources.stream()
                .filter(resource -> positiveCaseFilename.equals(resource.getFilename()))
                .findFirst()
                .orElse(null);

        assertNotNull(fileResource);
    }

    @Test
    void ensureBookParsingWorks() throws IOException {
        final var fileResource = booksResources.stream()
                .filter(resource -> positiveCaseFilename.equals(resource.getFilename()))
                .findFirst()
                .orElse(null);
        assertNotNull(fileResource);

        final var book = bookParser.parseJsonResource(fileResource);

        assertNotNull(book);
        assertNotNull(book.getTitle());
        assertNotNull(book.getId());
        assertNotNull(book.getAuthor());
        assertNotNull(book.getSections());
    }

    @Test
    void ensureBookSectionParsingWorks() throws IOException {
        final var fileResource = booksResources.stream()
                .filter(resource -> positiveCaseFilename.equals(resource.getFilename()))
                .findFirst()
                .orElse(null);
        assertNotNull(fileResource);

        final var book = bookParser.parseJsonResource(fileResource);

        assertNotNull(book.getSections());
        assertEquals(5, book.getSections().size());
        final var firstSection = book.getSections().getFirst();
        assertNotNull(firstSection.getId());
        assertNotNull(firstSection.getText());
        assertNotNull(firstSection.getType());
        assertNotNull(firstSection.getOptions());
    }

    @Test
    void ensureBookSectionOptionsParsingWorks() throws IOException {
        final var fileResource = booksResources.stream()
                .filter(resource -> positiveCaseFilename.equals(resource.getFilename()))
                .findFirst()
                .orElse(null);
        assertNotNull(fileResource);

        final var book = bookParser.parseJsonResource(fileResource);
        final var firstSection = book.getSections().getFirst();
        final var firstSectionOptions = firstSection.getOptions();
        assertNotNull(firstSectionOptions);
        assertNotEquals(0, firstSectionOptions.size());
        final var firstOption = firstSectionOptions.getFirst();
        assertNotNull(firstOption);
        assertNotNull(firstOption.getDescription());
        assertNotNull(firstOption.getGotoId());
    }
}