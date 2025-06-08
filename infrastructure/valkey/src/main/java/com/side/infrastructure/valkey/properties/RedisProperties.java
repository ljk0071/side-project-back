package com.side.infrastructure.valkey.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@ConfigurationProperties("redis")
public class RedisProperties {

	private String connectionIp;
	private String connectionPort;
	private String password;
	private long timeout;

}