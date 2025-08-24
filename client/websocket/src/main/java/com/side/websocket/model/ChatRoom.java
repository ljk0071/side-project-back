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

    private long partyRecruitId;
    private Long creatorId;

    private Instant createdAt;

    private Set<Long> participants;

    private boolean isActive;

    public void addParticipant(Long userId) {
        participants.add(userId);
    }

    public void removeParticipant(Long userId) {
        participants.remove(userId);
    }

    public int getParticipantCount() {
        return participants != null ? participants.size() : 0;
    }

}