package com.side.infrastructure.valkey.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("redis")
public record RedisProperties(
        String connectionIp,
        int connectionPort,
        String password,
        long timeout
) {
}