package com.side.rest.config;

import java.io.Serial;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration(proxyBeanMethods = false)
public class ObjectMapperConfig {

	@Bean
	public ObjectMapper objectMapper() {

		ObjectMapper mapper = new ObjectMapper() {

			@Serial
			private static final long serialVersionUID = 2857537290141783448L;

			@Override
			public String writeValueAsString(Object obj) {
				try {
					return super.writeValueAsString(obj);
				} catch (JsonProcessingException e) {
					log.error("[writeValueAsString Exception]", e);
					throw new IllegalArgumentException("Failed to convert object to JSON string", e);
				}
			}

			@Override
			public <T> T readValue(String str, Class<T> clazz) {
				return Optional.ofNullable(str)
							   .map(s -> {
								   try {
									   return super.readValue(s, clazz);
								   } catch (JsonProcessingException e) {
									   throw new IllegalArgumentException("Failed to convert JSON string to object", e);
								   }
							   })
							   .orElseThrow(() -> new IllegalArgumentException(
								   "Failed to convert JSON string to object: value is null or empty"
							   ));
			}

			@Override
			public <T> T readValue(String str, TypeReference<T> typeReference) {
				return Optional.ofNullable(str)
							   .map(s -> {
								   try {
									   return super.readValue(s, typeReference);
								   } catch (JsonProcessingException e) {
									   throw new IllegalArgumentException("Failed to convert JSON string to object", e);
								   }
							   })
							   .orElseThrow(() -> new IllegalArgumentException(
								   "Failed to convert JSON string to object: value is null or empty"
							   ));
			}
		};

		return mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

}
