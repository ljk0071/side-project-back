package com.side.infrastructure.valkey.cache.config;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.side.infrastructure.valkey.cache.constants.RedisCacheNames;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisCacheInitializer {

	private final CacheManager cacheManager;

	@EventListener
	public void run(ApplicationReadyEvent event) {

		List.of(
			RedisCacheNames.USER_STATUS,
			RedisCacheNames.USER_ROLES,
			RedisCacheNames.ROLE_HIERARCHY
		).forEach(this::cacheClear);

	}

	private void cacheClear(String cacheName) {
		Optional.ofNullable(cacheManager.getCache(cacheName)).ifPresent(Cache::clear);
	}

}
