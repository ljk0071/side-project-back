package com.side.domain.model;

import lombok.Builder;

import java.time.Instant;

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