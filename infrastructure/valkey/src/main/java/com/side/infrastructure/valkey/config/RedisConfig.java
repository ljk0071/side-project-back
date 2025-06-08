package com.side.infrastructure.valkey.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.side.infrastructure.valkey.properties.RedisProperties;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

	@Bean
	public RedisConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
		// Configure socket options
		SocketOptions socketOptions = SocketOptions.builder()
												   .connectTimeout(Duration.ofMillis(redisProperties.getTimeout()))
												   .build();

		// Configure client options
		ClientOptions clientOptions = ClientOptions.builder().socketOptions(socketOptions).build();

		// Configure Lettuce client
		LettuceClientConfiguration lettuceClientConfig = LettuceClientConfiguration.builder()
																				   .clientOptions(clientOptions)
																				   .build();

		// Create standalone configuration
		RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration();
		standaloneConfig.setHostName(redisProperties.getConnectionIp());
		standaloneConfig.setPort(Integer.parseInt(redisProperties.getConnectionPort()));
		standaloneConfig.setPassword(RedisPassword.of(redisProperties.getPassword()));

		// Create and return the connection factory
		return new LettuceConnectionFactory(standaloneConfig, lettuceClientConfig);
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

		return redisTemplate;
	}

}
