package com.side.domain.memory.service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.side.domain.memory.repository.MemoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemoryService {

	private final MemoryRepository memoryRepository;

	public Long increment(String key) {
		return memoryRepository.increment(key);
	}

	public void expire(String key, long timeOut, TimeUnit timeUnit) {
		memoryRepository.expire(key, timeOut, timeUnit);
	}

	public Long decrement(String key) {
		return memoryRepository.decrement(key);
	}

	public boolean insertIfAbsent(String key, Object value) {
		return memoryRepository.insertIfAbsent(key, value);
	}

	public void create(String key, Object value) {
		try {
			log.debug("MemoryService.create called - key: {}", key);
			memoryRepository.create(key, value);
			log.debug("MemoryService.create completed for key: {}", key);
		} catch (Exception e) {
			log.error("Error in MemoryService.create: {}", e.getMessage(), e);
			throw e;
		}
	}

	public void create(String key, Object value, long timeout, TimeUnit unit) {
		try {
			log.debug("MemoryService.create with timeout called - key: {}", key);
			memoryRepository.create(key, value, timeout, unit);
			log.debug("MemoryService.create with timeout completed for key: {}", key);
		} catch (Exception e) {
			log.error("Error in MemoryService.create with timeout: {}", e.getMessage(), e);
			throw e;
		}
	}

	public <T> T get(String key, Class<T> clazz) {
		try {
			log.debug("MemoryService.get called - key: {}, class: {}", key, clazz.getSimpleName());
			T result = memoryRepository.get(key, clazz);
			log.debug("MemoryService.get completed for key: {}", key);
			return result;
		} catch (Exception e) {
			log.error("Error in MemoryService.get: {}", e.getMessage(), e);
			throw e;
		}
	}

	public <T> T get(String key, TypeReference<T> typeReference) {
		try {
			log.debug("MemoryService.get with TypeReference called - key: {}", key);
			T result = memoryRepository.get(key, typeReference);
			log.debug("MemoryService.get with TypeReference completed for key: {}", key);
			return result;
		} catch (Exception e) {
			log.error("Error in MemoryService.get with TypeReference: {}", e.getMessage(), e);
			throw e;
		}
	}

	public <T> Optional<T> find(String key, Class<T> clazz) {
		try {
			log.debug("MemoryService.find called - key: {}, class: {}", key, clazz.getSimpleName());
			Optional<T> result = memoryRepository.find(key, clazz);
			log.debug("MemoryService.find completed for key: {}", key);
			return result;
		} catch (Exception e) {
			log.error("Error in MemoryService.find: {}", e.getMessage(), e);
			throw e;
		}
	}

	public <T> Optional<T> find(String key, TypeReference<T> typeReference) {
		try {
			log.debug("MemoryService.find with TypeReference called - key: {}", key);
			Optional<T> result = memoryRepository.find(key, typeReference);
			log.debug("MemoryService.find with TypeReference completed for key: {}", key);
			return result;
		} catch (Exception e) {
			log.error("Error in MemoryService.find with TypeReference: {}", e.getMessage(), e);
			throw e;
		}
	}

	public boolean delete(String key) {
		try {
			log.debug("MemoryService.delete called - key: {}", key);
			boolean result = memoryRepository.delete(key);
			log.debug("MemoryService.delete completed for key: {}, result: {}", key, result);
			return result;
		} catch (Exception e) {
			log.error("Error in MemoryService.delete: {}", e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Redis 연결을 테스트하는 메서드
	 *
	 * @return 연결 성공 여부
	 */
	public boolean testConnection() {
		try {
			log.info("Testing Redis connection...");
			// 간단한 값을 저장하고 바로 삭제하여 연결 테스트
			String testKey = "connection_test_" + System.currentTimeMillis();
			this.create(testKey, "OK");
			boolean deleted = this.delete(testKey);
			log.info("Redis connection test result: {}", deleted ? "Success" : "Failed");
			return deleted;
		} catch (Exception e) {
			log.error("Redis connection test failed: {}", e.getMessage(), e);
			return false;
		}
	}
}