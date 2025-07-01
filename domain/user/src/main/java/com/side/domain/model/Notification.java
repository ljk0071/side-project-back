package com.side.domain.model;

import java.time.Instant;

import com.side.domain.Metadata;

import lombok.Builder;

@Builder(toBuilder = true)
public record Notification(
	Long id,
	Long userUniqueId,
	String type,
	String title,
	String message,
	String targetType,
	Long targetId,
	Boolean isRead,
	Instant readAt,
	Metadata metadata
) {
}