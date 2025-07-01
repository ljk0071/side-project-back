package com.side.domain.model;

import lombok.Builder;

import java.time.Instant;

@Builder(toBuilder = true)
public record UserReaction(
        Long id,
        Long userUniqueId,
        String targetType,
        Long targetId,
        String reactionType,
        Boolean isDeleted,
        Instant createdAt,
        Instant deletedAt
) {
}