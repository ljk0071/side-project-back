package com.side.domain.model;

import java.time.Instant;

import lombok.Builder;

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