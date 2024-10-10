package com.cmorfe.banks.api.infrastructure.serialization;

import com.cmorfe.banks.api.domain.model.BankType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.io.IOException;

public class BankTypeDeserializer extends JsonDeserializer<BankType> {

    @Override
    public BankType deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String value = parser.getText();

        try {
            return BankType.valueOf(value);
        } catch (IllegalArgumentException exception) {
            throw new InvalidFormatException(parser, "Invalid value for BankType: " + value, value, BankType.class);
        }
    }
}
