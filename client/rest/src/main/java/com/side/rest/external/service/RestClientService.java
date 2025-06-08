package com.side.rest.external.service;

import java.lang.reflect.Field;
import java.net.URI;

import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.side.domain.HttpRequest;
import com.side.domain.ReflectionCache;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestClientService<O, T> implements HttpRequest<O, T> {

	private final RestClient restClient;

	public URI buildUriWithParams(String uri, O data) {

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

	@Override
	public void get(String uri, O data) {
		restClient.mutate()
				  .baseUrl(buildUriWithParams(uri, data))
				  .build()
				  .get()
				  .retrieve()
				  .toBodilessEntity();
	}

	@Override
	public T get(String uri, O data, Class<T> returnType) {

		return restClient.mutate()
						 .baseUrl(buildUriWithParams(uri, data))
						 .build()
						 .get()
						 .retrieve()
						 .body(returnType);
	}

	@Override
	public void post(String uri, O data) {

	}

	@Override
	public T post(String uri, O data, Class<T> returnType) {
		return restClient.mutate()
						 .baseUrl(uri)
						 .build()
						 .post()
						 .body(data)
						 .retrieve()
						 .body(returnType);
	}

	@Override
	public void patch(String uri, O data) {

	}

	@Override
	public void put(String uri, O data) {

	}

	@Override
	public void delete(String uri, O data) {

	}
}
