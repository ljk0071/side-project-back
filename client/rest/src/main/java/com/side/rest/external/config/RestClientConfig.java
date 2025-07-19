package com.side.rest.external.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.domain.ExternalApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.LaxRedirectStrategy;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@Configuration(proxyBeanMethods = false)
public class RestClientConfig {

    private final ObjectMapper objectMapper;

    public RestClientConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                         .requestFactory(createRequestFactory())
                         .messageConverters(converters -> {
                             converters.removeIf(MappingJackson2HttpMessageConverter.class::isInstance);
                             converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
                             converters.add(new FormHttpMessageConverter());
                         })
                         .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                         .defaultStatusHandler(HttpStatusCode::is4xxClientError, this::errorLogging4xx)
                         .defaultStatusHandler(HttpStatusCode::is5xxServerError, this::errorLogging5xx)
                         .build();
    }

    private ClientHttpRequestFactory createRequestFactory() {

        var httpClient = HttpClients.custom()
                                    .setRedirectStrategy(LaxRedirectStrategy.INSTANCE)
                                    .build();

        var httpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        httpRequestFactory.setConnectTimeout(Duration.ofSeconds(5));
        httpRequestFactory.setReadTimeout(Duration.ofSeconds(5));

        return httpRequestFactory;
    }

    private void errorLogging4xx(HttpRequest request, ClientHttpResponse response) throws IOException {
        String errorMessage = new String(response.getBody().readAllBytes());
        log.error("Client Error Code : {}", response.getStatusCode());
        log.error("Client Error Message : {}", errorMessage);
        throw new ExternalApiException(response.getStatusCode().value(), errorMessage);
    }

    private void errorLogging5xx(HttpRequest request, ClientHttpResponse response) throws IOException {
        String errorMessage = new String(response.getBody().readAllBytes());
        log.error("Server Error Code : {}", response.getStatusCode());
        log.error("Server Error Message : {}", errorMessage);
        throw new ExternalApiException(response.getStatusCode().value(), errorMessage);
    }
}
