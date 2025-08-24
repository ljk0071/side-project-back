package com.side.websocket.model;

import com.side.domain.enums.PartyApplicationStatusTypeEnum;
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
    private long partyRecruitId;
    private Long senderId;
    private String senderName;
    private String contents;
    private boolean application;
    private PartyApplicationStatusTypeEnum statusType;
    private MessageType type;

    @Builder.Default
    private Instant timestamp = Instant.now();

    @Builder.Default
    private boolean isDeleted = false;

    public static ChatMessage notifyApplication(long partyRecruitId, String contents) {
        return ChatMessage.builder()
                          .messageId(generateMessageId())
                          .partyRecruitId(partyRecruitId)
                          .senderId(0L)
                          .senderName("System")
                          .contents(contents)
                          .application(true)
                          .type(MessageType.SYSTEM)
                          .build();
    }

    public static ChatMessage createSystemMessage(long partyRecruitId, String content) {
        return ChatMessage.builder()
                          .messageId(generateMessageId())
                          .partyRecruitId(partyRecruitId)
                          .senderId(0L)
                          .senderName("System")
                          .contents(content)
                          .application(false)
                          .type(MessageType.SYSTEM)
                          .build();
    }

    public static ChatMessage createJoinMessage(long partyRecruitId, String userName) {
        return ChatMessage.builder()
                          .messageId(generateMessageId())
                          .partyRecruitId(partyRecruitId)
                          .senderId(0L)
                          .senderName("System")
                          .contents(userName + "님이 입장했습니다.")
                          .application(false)
                          .type(MessageType.JOIN)
                          .build();
    }

    public static ChatMessage notifyToSubscribe(long partyRecruitId, long userUniqueId, PartyApplicationStatusTypeEnum statusType) {
        return ChatMessage.builder()
                          .messageId(generateMessageId())
                          .partyRecruitId(partyRecruitId)
                          .senderId(userUniqueId)
                          .senderName(null)
                          .contents(null)
                          .application(false)
                          .statusType(statusType)
                          .type(MessageType.NOTIFICATION)
                          .build();
    }

    public static ChatMessage createLeaveMessage(long partyRecruitId, Long userId, String userName) {
        return ChatMessage.builder()
                          .messageId(generateMessageId())
                          .partyRecruitId(partyRecruitId)
                          .senderId(userId)
                          .senderName(userName)
                          .contents(userName + "님이 퇴장했습니다.")
                          .application(false)
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