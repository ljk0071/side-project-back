package com.side.rest.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
@Builder
public class UserReactionRecordResponseDto {

    private Long id;
    private Long userUniqueId;
    private String targetType;
    private Long targetId;
    private String reactionType;
    private Boolean isDeleted;
    private Instant createdAt;
    private Instant deletedAt;
}