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
public class NotificationDto {

    public enum NotificationType {
        PARTY_APPLICATION_RECEIVED,  // 파티 지원 접수
        PARTY_APPLICATION_ACCEPTED,  // 파티 지원 승인
        PARTY_APPLICATION_REJECTED,  // 파티 지원 거절
        PARTY_CHAT_INVITE,          // 파티 채팅방 초대
        SYSTEM_ANNOUNCEMENT         // 시스템 공지
    }

    private String id;
    private NotificationType type;
    private String title;
    private String message;
    private Long relatedPartyId;
    private Long relatedUserId;
    private boolean read;
    
    @Builder.Default
    private Instant createdAt = Instant.now();
}