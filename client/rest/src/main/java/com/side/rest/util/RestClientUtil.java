package com.side.rest.util;

import com.side.domain.HttpRequest;
import com.side.domain.ReflectionCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RestClientUtil implements HttpRequest {

    private final RestClient restClient;

    private <O> URI buildUriWithParams(String uri, O data) {

        if (data == null) {
            return URI.create(uri);
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uri);

        ReflectionCache.getFields(data)
                       .ifPresent(fields -> {
                           for (Field field : fields) {
                               Object value = ReflectionUtils.getField(field, data);
                               if (value != null) {
                                   builder.queryParam(field.getName(), value);
                               }
                           }
                       });

        return builder.build().toUri();
    }

    private URI buildUriWithParams(String uri, Map<String, Object> data) {

        if (data == null) {
            return URI.create(uri);
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uri);

        data.forEach(builder::queryParam);

        return builder.build().toUri();
    }

    @Override
    public <O, T> T get(String url, Map<String, String> headers, O data, Class<T> returnType) {

        var restClientBuilder = restClient.mutate()
                                          .baseUrl(buildUriWithParams(url, data));

        if (!headers.isEmpty()) {
            headers.forEach(restClientBuilder::defaultHeader);
        }

        return restClientBuilder.build()
                                .get()
                                .retrieve()
                                .body(returnType);
    }

    @Override
    public <O, T> T post(String url, Map<String, String> headers, O data, Class<T> returnType) {

        var restClientBuilder = restClient.mutate()
                                          .baseUrl(url);

        if (!headers.isEmpty()) {
            headers.forEach(restClientBuilder::defaultHeader);
        }

        return restClientBuilder.build()
                                .post()
                                .body(data)
                                .retrieve()
                                .body(returnType);
    }

    @Override
    public <O, T> T patch(String url, Map<String, String> headers, O data, Class<T> returnType) {
        var restClientBuilder = restClient.mutate()
                                          .baseUrl(url);

        if (!headers.isEmpty()) {
            headers.forEach(restClientBuilder::defaultHeader);
        }

        return restClientBuilder.build()
                                .patch()
                                .body(data)
                                .retrieve()
                                .body(returnType);
    }

    @Override
    public <O, T> T put(String url, Map<String, String> headers, O data, Class<T> returnType) {
        var restClientBuilder = restClient.mutate()
                                          .baseUrl(url);

        if (!headers.isEmpty()) {
            headers.forEach(restClientBuilder::defaultHeader);
        }

        return restClientBuilder.build()
                                .put()
                                .body(data)
                                .retrieve()
                                .body(returnType);
    }

    @Override
    public <O, T> T delete(String url, Map<String, String> headers, O data, Class<T> returnType) {
        var restClientBuilder = restClient.mutate()
                                          .baseUrl(url);

        if (!headers.isEmpty()) {
            headers.forEach(restClientBuilder::defaultHeader);
        }

        return restClientBuilder.build()
                                .delete()
                                .retrieve()
                                .body(returnType);
    }
}
