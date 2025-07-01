package com.side.rest.domain.user.dto.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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