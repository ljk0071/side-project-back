package com.side.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    private String messageId;
    private String roomId;
    private Long senderId;
    private String senderName;
    private String content;
    private MessageType type;

    @Builder.Default
    private Instant timestamp = Instant.now();

    @Builder.Default
    private boolean isDeleted = false;

    public static ChatMessage createSystemMessage(String roomId, String content) {
        return ChatMessage.builder()
                          .messageId(generateMessageId())
                          .roomId(roomId)
                          .senderId(0L)
                          .senderName("System")
                          .content(content)
                          .type(MessageType.SYSTEM)
                          .build();
    }

    public static ChatMessage createJoinMessage(String roomId, Long userId, String userName) {
        return ChatMessage.builder()
                          .messageId(generateMessageId())
                          .roomId(roomId)
                          .senderId(userId)
                          .senderName(userName)
                          .content(userName + "님이 입장했습니다.")
                          .type(MessageType.JOIN)
                          .build();
    }

    public static ChatMessage createLeaveMessage(String roomId, Long userId, String userName) {
        return ChatMessage.builder()
                          .messageId(generateMessageId())
                          .roomId(roomId)
                          .senderId(userId)
                          .senderName(userName)
                          .content(userName + "님이 퇴장했습니다.")
                          .type(MessageType.LEAVE)
                          .build();
    }

    private static String generateMessageId() {
        return "MSG_" + System.currentTimeMillis() + "_" + System.nanoTime();
    }

    public enum MessageType {
        CHAT,       // 일반 채팅
        JOIN,       // 입장
        LEAVE,      // 퇴장
        SYSTEM,     // 시스템 메시지
        NOTIFICATION // 알림
    }
}