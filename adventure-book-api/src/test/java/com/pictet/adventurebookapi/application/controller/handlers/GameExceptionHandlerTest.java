package com.pictet.adventurebookapi.application.controller.handlers;

import com.pictet.adventurebookapi.application.controller.GameController;
import com.pictet.adventurebookapi.application.dto.CreateGameDTO;
import com.pictet.adventurebookapi.domain.exceptions.InvalidBookException;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameExceptionHandlerTest {

    private final GameExceptionHandler handler = new GameExceptionHandler();

    @Test
    void ensureSingleFieldErrorReturnsBadRequest() throws Exception {
        final var exception = validationException(new FieldError("request", "playerId", "must not be blank"));

        final var problem = handler.handleValidation(exception);

        assertEquals(400, problem.getStatus());
        assertEquals("Invalid request payload", problem.getDetail());
        assertEquals(Map.of("playerId", List.of("must not be blank")), problem.getProperties().get("errors"));
    }

    @Test
    void ensureDifferentFieldsHaveSeparateMessages() throws Exception {
        final var exception = validationException(
                new FieldError("request", "playerId", "must not be blank"),
                new FieldError("request", "bookId", "must not be null"));

        final var problem = handler.handleValidation(exception);

        assertEquals(Map.of(
                "playerId", List.of("must not be blank"),
                "bookId", List.of("must not be null")), problem.getProperties().get("errors"));
    }

    @Test
    void ensureMultipleMessagesForSameFieldArePreserved() throws Exception {
        final var exception = validationException(
                new FieldError("request", "playerId", "must not be blank"),
                new FieldError("request", "playerId", "invalid format"));

        final var problem = handler.handleValidation(exception);

        assertEquals(Map.of("playerId", List.of("must not be blank", "invalid format")),
                problem.getProperties().get("errors"));
    }

    @Test
    void ensureNoFieldErrorsReturnsEmptyErrors() throws Exception {
        final var problem = handler.handleValidation(validationException());

        assertEquals(Map.of(), problem.getProperties().get("errors"));
    }

    @Test
    void ensureInterleavedErrorsAreGroupedWithoutLosingMessageOrder() throws Exception {
        final var exception = validationException(
                new FieldError("request", "playerId", "must not be blank"),
                new FieldError("request", "bookId", "must not be null"),
                new FieldError("request", "playerId", "invalid format"));

        final var problem = handler.handleValidation(exception);

        assertEquals(Map.of(
                "playerId", List.of("must not be blank", "invalid format"),
                "bookId", List.of("must not be null")), problem.getProperties().get("errors"));
    }

    @Test
    void ensureValidationErrorsAreNotSharedBetweenRequests() throws Exception {
        final var first = handler.handleValidation(validationException(
                new FieldError("request", "playerId", "must not be blank")));
        final var second = handler.handleValidation(validationException(
                new FieldError("request", "bookId", "must not be null")));

        assertEquals(Map.of("playerId", List.of("must not be blank")), first.getProperties().get("errors"));
        assertEquals(Map.of("bookId", List.of("must not be null")), second.getProperties().get("errors"));
    }

    @Test
    void ensureUnreadablePayloadReturnsBadRequestWithoutParserDetails() {
        final var exception = new HttpMessageNotReadableException(
                "Internal parser details", new MockHttpInputMessage(new byte[0]));

        final var problem = handler.handleUnreadablePayload(exception);

        assertEquals(400, problem.getStatus());
        assertEquals("Request body must be valid JSON with the expected field types", problem.getDetail());
    }

    @Test
    void ensureMissingGameReturnsNotFound() {
        final var problem = handler.handleNotFound(new NoSuchElementException("Game not found"));

        assertEquals(404, problem.getStatus());
        assertEquals("Game not found", problem.getDetail());
    }

    @Test
    void ensureIllegalChoiceReturnsBadRequest() {
        final var problem = handler.handleBadRequest(new IllegalArgumentException("You're not allowed to choose this option"));

        assertEquals(400, problem.getStatus());
        assertEquals("You're not allowed to choose this option", problem.getDetail());
    }

    @Test
    void ensureFinishedGameReturnsConflict() {
        final var problem = handler.handleConflict(new IllegalStateException("Game has ended"));

        assertEquals(409, problem.getStatus());
        assertEquals("Game has ended", problem.getDetail());
    }

    @Test
    void ensureInvalidBookReturnsInternalServerError() {
        final var problem = handler.handleInvalidBook(new InvalidBookException("Misconfigured book: has no begin section"));

        assertEquals(500, problem.getStatus());
        assertEquals("Misconfigured book: has no begin section", problem.getDetail());
    }

    private static MethodArgumentNotValidException validationException(final FieldError... errors) throws Exception {
        final var bindingResult = new BeanPropertyBindingResult(new CreateGameDTO(), "request");
        for (final var error : errors) {
            bindingResult.addError(error);
        }
        final var method = GameController.class.getMethod("newGame", CreateGameDTO.class);
        return new MethodArgumentNotValidException(new MethodParameter(method, 0), bindingResult);
    }
}
