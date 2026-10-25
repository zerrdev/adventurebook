package com.pictet.adventurebookapi.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void ensureBookDetailsExposeOnlyMetadataAndUnreachableSectionIds() throws Exception {
        final var response = mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        final var details = objectMapper.readTree(response.getContentAsString());
        final var fields = new HashSet<String>();
        details.fieldNames().forEachRemaining(fields::add);

        assertEquals(Set.of("id", "title", "author", "valid", "errors", "unreachableSections"), fields);
        assertEquals(1, details.get("id").asInt());
        assertTrue(details.get("valid").asBoolean());
        assertTrue(details.get("errors").isArray());
        assertTrue(details.get("unreachableSections").isArray());
    }

    @Test
    void ensureUnknownBookReturnsNotFound() throws Exception {
        mockMvc.perform(get("/books/2147483647"))
                .andExpect(status().isNotFound());
    }
}
