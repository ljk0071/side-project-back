package com.side.security.jwt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("security.jwt")
public record JwtProperties(
	String secretKey,
	long expirationTime,
	long refreshExpirationTime,
	long expirationSeconds,
	long refreshExpirationSeconds
) {
}