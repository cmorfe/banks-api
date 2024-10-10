package com.cmorfe.banks.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;

public class JsonResultMatchers {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> ResultMatcher jsonEquals(T expectedObject) {
        return result -> {
            try {
            String expectedJson = objectMapper.writeValueAsString(expectedObject);

            MockMvcResultMatchers.content().json(expectedJson).match(result);
            } catch (IOException e) {
                throw new RuntimeException("Failed to process JSON", e);
            }
        };
    }
}