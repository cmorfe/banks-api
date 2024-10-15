package com.cmorfe.banks.api.infrastructure.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class BankTypeDeserializerTest {

    private final BankTypeDeserializer deserializer = new BankTypeDeserializer();

    @Test
    void shouldThrowInvalidFormatExceptionForInvalidValue() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        when(parser.getText()).thenReturn("INVALID_VALUE");

        assertThrows(InvalidFormatException.class, () -> deserializer.deserialize(parser, context));
    }
}