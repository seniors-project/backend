package com.seniors.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.Strings;
import com.seniors.common.exception.JsonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JsonService {
	private final ObjectMapper objectMapper;

	public String objectToString(Object object) {
		if (object == null) {
			return null;
		}
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new JsonException(
					String.format("json object to string error. (object:%s) %s",
							object, e.getMessage()));
		}
	}

	public <T> T stringToObject(String content, Class<T> classType) {
		if (Strings.isNullOrEmpty(content)) {
			return null;
		}

		try {
			return objectMapper.readValue(content, classType);
		} catch (JsonProcessingException e) {
			throw new JsonException(
					String.format("json string to object error. (string:%s, class:%s). %s", content,
							classType.getName(), e.getMessage()));
		}
	}

	public <T> T stringToObject(String content, TypeReference<T> typeReference) {
		if (Strings.isNullOrEmpty(content)) {
			return null;
		}

		try {
			return objectMapper.readValue(content, typeReference);
		} catch (JsonProcessingException e) {
			throw new JsonException(
					String.format("json string to object error. (string:%s, class:%s). %s", content,
							typeReference.getType().getTypeName(), e.getMessage()));
		}
	}

	public <T> List<T> stringToListObject(String content, Class<T> classType)
			throws JsonProcessingException {
		if (Strings.isNullOrEmpty(content)) {
			return Collections.emptyList();
		}

		objectMapper.registerModule(new JavaTimeModule());
		CollectionType listType = objectMapper.getTypeFactory()
				.constructCollectionType(ArrayList.class, classType);
		return objectMapper.readValue(content, listType);
	}
}
