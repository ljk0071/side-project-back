package com.side.rest.domain.user.dto.response;

import java.time.Instant;

import com.side.rest.domain.board.dto.response.MetadataResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class NotificationResponseDto {

    private long id;
    private long userUniqueId;
    private String type;
    private String title;
    private String message;
    private String targetType;
    private long targetId;
    private boolean isRead;
    private Instant readAt;
    private MetadataResponseDto metadata;
}