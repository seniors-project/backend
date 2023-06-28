package com.side.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JsonConfig {

	/* ref: https://tech.kakaopay.com/post/martin-dev-honey-tip-1/
	 * POST, PUT method QueryParam 공백 제거
	 */
	public ObjectMapper objectMapper() {
		return Jackson2ObjectMapperBuilder
				.json()
				.modules(customJsonDeserializeModule())
				.build();
	}

	private SimpleModule customJsonDeserializeModule() {
		SimpleModule module = new SimpleModule();
		module.addDeserializer(String.class, new StringStripJsonDeserializer());

		return module;
	}
}
