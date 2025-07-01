package com.side.domain.model;

import com.side.domain.Metadata;
import lombok.Builder;

import java.time.Instant;

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