package com.side.websocket.dto;

import com.side.websocket.model.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {

    private String messageId;
    private long partyRecruitId;
    private Long senderId;
    private String senderName;
    private String contents;
    private ChatMessage.MessageType type;
    private Instant timestamp;
    private boolean isDeleted;

    public static ChatMessageDto from(ChatMessage message) {
        return ChatMessageDto.builder()
                             .messageId(message.getMessageId())
                             .partyRecruitId(message.getPartyRecruitId())
                             .senderId(message.getSenderId())
                             .senderName(message.getSenderName())
                             .contents(message.getContents())
                             .type(message.getType())
                             .timestamp(message.getTimestamp())
                             .isDeleted(message.isDeleted())
                             .build();
    }

    public ChatMessage toEntity() {
        return ChatMessage.builder()
                          .messageId(this.messageId)
                          .partyRecruitId(this.partyRecruitId)
                          .senderId(this.senderId)
                          .senderName(this.senderName)
                          .contents(this.contents)
                          .type(this.type)
                          .timestamp(this.timestamp)
                          .isDeleted(this.isDeleted)
                          .build();
    }
}