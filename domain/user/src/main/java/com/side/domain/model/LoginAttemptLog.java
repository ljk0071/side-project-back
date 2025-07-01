package com.side.domain.model;

import java.time.Instant;

import lombok.Builder;

@Builder(toBuilder = true)
public record LoginAttemptLog(
	Long id,
	String userId,
	String ipAddress,
	String userAgent,
	Boolean isSucceeded,
	String failureReason,
	Instant createdAt
) {
}