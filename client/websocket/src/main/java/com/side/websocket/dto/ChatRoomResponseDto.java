package com.side.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomResponseDto {

    private long partyRecruitId;
    private Long creatorId;
    private int participantCount;
    private boolean isActive;
    private Instant createdAt;
}