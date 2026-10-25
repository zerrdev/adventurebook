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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ExplorationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void ensureInvalidBooksCanBeExploredWithoutExposingConsequences() throws Exception {
        final var index = mockMvc.perform(get("/books/3/explore"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertFalse(objectMapper.readTree(index).isEmpty());

        final var response = mockMvc.perform(get("/books/3/explore/1"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        final var node = objectMapper.readTree(response);
        assertEquals(1, node.get("id").asInt());
        assertEquals(50011, node.get("options").get(0).get("gotoId").asInt());
        for (final var option : node.get("options")) {
            final var fields = new HashSet<String>();
            option.fieldNames().forEachRemaining(fields::add);
            assertEquals(Set.of("description", "gotoId"), fields);
        }
        // This node has a health consequence in the book; exploration must omit it.
        final var choice = mockMvc.perform(get("/books/1/explore/20"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertFalse(choice.contains("consequence"));
        assertFalse(choice.contains("healthPoints"));
    }

    @Test
    void ensureMissingTargetsAndBooksReturnNotFound() throws Exception {
        mockMvc.perform(get("/books/3/explore/50011")).andExpect(status().isNotFound());
        mockMvc.perform(get("/books/2147483647/explore")).andExpect(status().isNotFound());
        mockMvc.perform(get("/books/2147483647/explore/1")).andExpect(status().isNotFound());
    }
}
