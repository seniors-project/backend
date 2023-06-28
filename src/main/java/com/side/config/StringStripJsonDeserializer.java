package com.side.config;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class StringStripJsonDeserializer extends JsonDeserializer<String> {

	@Override
	public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
		String value = p.getValueAsString();

		if (value == null) {
			return null;
		}

		String valueStripped = value.strip();

		return valueStripped.length() != 0 ? valueStripped : null;
	}
}
