package com.side.domain;

import java.time.Instant;

import lombok.Builder;

@Builder(toBuilder = true)
public record Metadata(long createdBy,
					   String createdByName,
					   Instant createdAt,
					   Long modifiedBy,
					   String modifiedByName,
					   Instant modifiedAt) {
}