package com.side.infrastructure.valkey.repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.domain.memory.repository.MemoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Repository
@RequiredArgsConstructor
public class RedisRepositoryImpl implements MemoryRepository {

	private final RedisTemplate<String, String> redisTemplate;
	private final ObjectMapper objectMapper;

	public Long increment(String key) {
		return redisTemplate
				   .opsForValue()
				   .increment(key, 1);
	}

	public void expire(String key, long timeOut, TimeUnit timeUnit) {
		redisTemplate
			.expire(key, timeOut, timeUnit);
	}

	public Long decrement(String key) {
		return redisTemplate
				   .opsForValue()
				   .increment(key, -1);
	}

	@SneakyThrows
	public boolean insertIfAbsent(String key, Object value) {
		return Boolean.TRUE == redisTemplate.opsForValue()
											.setIfAbsent(key, objectMapper.writeValueAsString(value));
	}

	@SneakyThrows
	public void create(String key, Object value) {
		redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(value));
	}

	@SneakyThrows
	public void create(String key, Object value, long timeout, TimeUnit unit) {
		redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(value), timeout, unit);
	}

	@SneakyThrows
	public <T> T get(String key, Class<T> clazz) {

		String value = Optional.ofNullable(redisTemplate.opsForValue().get(key))
							   .orElseThrow(() -> new IllegalArgumentException("No value found for key: " + key));

		return objectMapper.readValue(value, clazz);
	}

	@SneakyThrows
	public <T> T get(String key, TypeReference<T> typeReference) {

		String value = Optional.ofNullable(redisTemplate.opsForValue().get(key))
							   .orElseThrow(() -> new IllegalArgumentException("No value found for key: " + key));

		return objectMapper.readValue(value, typeReference);
	}

	@SneakyThrows
	public <T> Optional<T> find(String key, Class<T> clazz) {

		String data = redisTemplate.opsForValue().get(key);

		if (data == null) {
			return Optional.empty();
		}

		return Optional.of(objectMapper.readValue(data, clazz));
	}

	@SneakyThrows
	public <T> Optional<T> find(String key, TypeReference<T> typeReference) {

		String data = redisTemplate.opsForValue().get(key);

		if (data == null) {
			return Optional.empty();
		}

		return Optional.of(objectMapper.readValue(data, typeReference));
	}

	public boolean delete(String key) {
		return redisTemplate.delete(key);
	}
}
