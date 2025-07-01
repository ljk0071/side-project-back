package com.side.domain;

import lombok.Builder;

import java.time.Instant;

@Builder(toBuilder = true)
public record Metadata(
        long createdBy,
        String createdByName,
        Instant createdAt,
        Long modifiedBy,
        String modifiedByName,
        Instant modifiedAt,
        Long deletedBy,
        String deletedByName,
        Instant deletedAt
) {
}