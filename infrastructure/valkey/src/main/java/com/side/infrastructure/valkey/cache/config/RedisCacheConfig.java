package com.side.infrastructure.valkey.cache.config;

import java.time.Duration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisCacheConfig {

	@Bean
	public RedisCacheConfiguration redisCacheConfiguration() {
		return RedisCacheConfiguration.defaultCacheConfig()
									  .entryTtl(Duration.ofHours(1L))
									  .disableCachingNullValues()
									  .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(
										  new StringRedisSerializer()))
									  .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
										  new GenericJackson2JsonRedisSerializer()));
	}

}
