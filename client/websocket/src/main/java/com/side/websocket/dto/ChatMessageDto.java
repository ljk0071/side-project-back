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
    private String roomId;
    private Long senderId;
    private String senderName;
    private String content;
    private ChatMessage.MessageType type;
    private Instant timestamp;
    private boolean isDeleted;

    public static ChatMessageDto from(ChatMessage message) {
        return ChatMessageDto.builder()
                             .messageId(message.getMessageId())
                             .roomId(message.getRoomId())
                             .senderId(message.getSenderId())
                             .senderName(message.getSenderName())
                             .content(message.getContent())
                             .type(message.getType())
                             .timestamp(message.getTimestamp())
                             .isDeleted(message.isDeleted())
                             .build();
    }

    public ChatMessage toEntity() {
        return ChatMessage.builder()
                          .messageId(this.messageId)
                          .roomId(this.roomId)
                          .senderId(this.senderId)
                          .senderName(this.senderName)
                          .content(this.content)
                          .type(this.type)
                          .timestamp(this.timestamp)
                          .isDeleted(this.isDeleted)
                          .build();
    }
}