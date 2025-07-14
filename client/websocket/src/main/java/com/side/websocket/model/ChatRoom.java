package com.side.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {

    private String roomId;
    private String roomName;
    private Long creatorId;

    @Builder.Default
    private Instant createdAt = Instant.now();

    @Builder.Default
    private Set<Long> participants = ConcurrentHashMap.newKeySet();

    @Builder.Default
    private int maxParticipants = 100;

    @Builder.Default
    private boolean isActive = true;

    public void addParticipant(Long userId) {
        participants.add(userId);
    }

    public void removeParticipant(Long userId) {
        participants.remove(userId);
    }

    public int getParticipantCount() {
        return participants.size();
    }

    public boolean isFull() {
        return participants.size() >= maxParticipants;
    }
}