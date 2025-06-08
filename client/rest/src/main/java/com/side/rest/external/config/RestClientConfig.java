package com.side.rest.external.config;

import java.time.Duration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.domain.ExternalApiException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration(proxyBeanMethods = false)
public class RestClientConfig {

	private final ObjectMapper objectMapper;

	private final RestClient restClient;

	public RestClientConfig(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		this.restClient = createRestClient();
	}

	private ClientHttpRequestFactory createRequestFactory() {

		SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
		simpleClientHttpRequestFactory.setConnectTimeout(Duration.ofSeconds(5));
		simpleClientHttpRequestFactory.setReadTimeout(Duration.ofSeconds(5));

		return simpleClientHttpRequestFactory;
	}

	private RestClient createRestClient() {

		return RestClient.builder()
						 .requestFactory(createRequestFactory())
						 .messageConverters(converters -> {
							 converters.removeIf(MappingJackson2HttpMessageConverter.class::isInstance);
							 converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
						 })
						 .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
						 .defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> {
							 log.error("Client Error Code : {}", response.getStatusCode());
							 log.error("Client Error Message : {}", new String(response.getBody().readAllBytes()));
							 throw new ExternalApiException(response.getStatusCode().value(),
								 new String(response.getBody().readAllBytes()));
						 })
						 .defaultStatusHandler(HttpStatusCode::is5xxServerError, (request, response) -> {
							 log.error("Server Error Code : {}", response.getStatusCode());
							 log.error("Server Error Message : {}", new String(response.getBody().readAllBytes()));
							 throw new ExternalApiException(response.getStatusCode().value(),
								 new String(response.getBody().readAllBytes()));
						 })
						 .build();
	}
}
