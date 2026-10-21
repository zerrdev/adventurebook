package com.pictet.adventurebookapi.domain.validators.validations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pictet.adventurebookapi.domain.model.Book;
import com.pictet.adventurebookapi.domain.model.BookSection;
import com.pictet.adventurebookapi.domain.model.BookSectionOption;
import com.pictet.adventurebookapi.infrastructure.serialization.JsonBookParser;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookSectionValidationsTest {

    private final BookSectionValidations bookSectionValidations = new BookSectionValidations();

    private final JsonBookParser jsonBookParser = new JsonBookParser(new ObjectMapper());

    @Test
    void ensureValidBookHasNoErrors() {
        final var book = book(
                section(1, 2, 3),
                section(2, 3),
                section(3));

        assertTrue(bookSectionValidations.validate(book).isEmpty());
    }

    @Test
    void ensureEmptySectionsAreReported() {
        final var book = book();

        assertEquals(List.of("Sections cannot be empty"), bookSectionValidations.validate(book));
    }

    @Test
    void ensureDuplicateSectionIdIsReported() {
        final var book = book(
                section(1, 2),
                section(2),
                section(2));

        assertEquals(List.of("Duplicate section ID: 2"), bookSectionValidations.validate(book));
    }

    @Test
    void ensureEachExtraDuplicateIsReported() {
        final var book = book(
                section(1),
                section(1),
                section(1));

        assertEquals(List.of("Duplicate section ID: 1", "Duplicate section ID: 1"),
                bookSectionValidations.validate(book));
    }

    @Test
    void ensureUnknownGotoIdIsReported() {
        final var book = book(
                section(1, 2, 99),
                section(2));

        assertEquals(List.of("Invalid section gotoId: 99"), bookSectionValidations.validate(book));
    }

    @Test
    void ensureGotoIdToLaterSectionIsValid() {
        // gotoIds are checked after all sections are collected, so forward references are allowed
        final var book = book(
                section(1, 1000),
                section(1000));

        assertTrue(bookSectionValidations.validate(book).isEmpty());
    }

    @Test
    void ensureSectionsWithoutOptionsAreAccepted() {
        final var endSection = section(2);
        endSection.setOptions(null);
        final var book = book(section(1, 2), endSection);

        assertDoesNotThrow(() -> bookSectionValidations.validate(book));
        assertTrue(bookSectionValidations.validate(book).isEmpty());
    }

    @Test
    void ensureAllErrorsAreCollected() {
        final var book = book(
                section(1, 7),
                section(1, 8));

        assertEquals(List.of(
                        "Duplicate section ID: 1",
                        "Invalid section gotoId: 7",
                        "Invalid section gotoId: 8"),
                bookSectionValidations.validate(book));
    }

    @Test
    void ensureValidFixtureHasNoErrors() {
        final var book = parse("books/the-prisoner.json");

        assertTrue(bookSectionValidations.validate(book).isEmpty());
    }

    @Test
    void ensureBadIdFixtureReportsInvalidGotoId() {
        final var book = parse("books/the-prisoner_bad-id.json");

        assertEquals(List.of("Invalid section gotoId: 50011"), bookSectionValidations.validate(book));
    }

    private Book parse(final String path) {
        return jsonBookParser.parseBookResource(new ClassPathResource(path)).orElseThrow();
    }

    private static Book book(final BookSection... sections) {
        final var book = new Book();
        book.setId(1);
        book.setTitle("Test book");
        book.setSections(List.of(sections));
        return book;
    }

    private static BookSection section(final int id, final Integer... gotoIds) {
        final var section = new BookSection();
        section.setId(id);
        section.setOptions(Arrays.stream(gotoIds).map(BookSectionValidationsTest::option).toList());
        return section;
    }

    private static BookSectionOption option(final Integer gotoId) {
        final var option = new BookSectionOption();
        option.setGotoId(gotoId);
        return option;
    }
}
