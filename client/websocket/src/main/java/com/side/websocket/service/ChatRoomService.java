package com.side.websocket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.websocket.model.ChatMessage;
import com.side.websocket.model.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisSubscriber redisSubscriber;
    private final ObjectMapper objectMapper;
    private final JdbcClient jdbcClient;

    private final Map<Long, ChannelTopic> channelTopics = new ConcurrentHashMap<>();

    public ChatRoom createChatRoom(long partyRecruitId, Long creatorId) {
        ChatRoom chatRoom = ChatRoom.builder()
                                    .partyRecruitId(partyRecruitId)
                                    .creatorId(creatorId)
                                    .build();

        redisTemplate.opsForHash().put("CHAT_ROOM", String.valueOf(partyRecruitId), chatRoom);

        log.info("채팅방 생성: {}", partyRecruitId);
        return chatRoom;
    }

    public ChatRoom findRoomById(String roomId) {
        Object roomData = redisTemplate.opsForHash().get("CHAT_ROOM", roomId);
        if (roomData == null) {
            return null;
        }

        try {
            return objectMapper.convertValue(roomData, ChatRoom.class);
        } catch (Exception e) {
            log.error("ChatRoom 변환 실패: roomId={}", roomId, e);
            return null;
        }
    }

    public Set<String> findAllRoomIds() {
        return redisTemplate.opsForHash().keys("CHAT_ROOM")
                            .stream()
                            .map(Object::toString)
                            .collect(Collectors.toSet());
    }

    public List<ChatRoom> findAllRooms() {
        return redisTemplate.opsForHash().values("CHAT_ROOM")
                            .stream()
                            .map(this::convertToChatRoom)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
    }

    private ChatRoom convertToChatRoom(Object obj) {
        if (obj == null) {
            return null;
        }

        try {
            if (obj instanceof ChatRoom) {
                return (ChatRoom) obj;
            } else {
                // LinkedHashMap -> ChatRoom 변환
                return objectMapper.convertValue(obj, ChatRoom.class);
            }
        } catch (Exception e) {
            log.error("ChatRoom 변환 실패: {}", obj, e);
            return null;
        }
    }

    public boolean isAlreadyJoined(long partyRecruitId, long userId) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember("CHAT_ROOM_USERS:" + partyRecruitId, userId));
    }

    public void enterChatRoom(long partyRecruitId, Long userId) {
        String channelName = "chat." + partyRecruitId;
        ChannelTopic topic = channelTopics.computeIfAbsent(partyRecruitId, k -> new ChannelTopic(channelName));

        redisMessageListenerContainer.addMessageListener(redisSubscriber, topic);
        redisTemplate.opsForSet().add("CHAT_ROOM_USERS:" + partyRecruitId, userId);

        log.info("사용자 {}가 채팅방 {}에 입장", userId, partyRecruitId);
    }

    public void leaveChatRoom(long partyRecruitId, Long userId) {
        redisTemplate.opsForSet().remove("CHAT_ROOM_USERS:" + partyRecruitId, userId);

        if (redisTemplate.opsForSet().size("CHAT_ROOM_USERS:" + partyRecruitId) == 0) {
            ChannelTopic topic = channelTopics.remove(partyRecruitId);
            if (topic != null) {
                redisMessageListenerContainer.removeMessageListener(redisSubscriber, topic);
            }
        }

        log.info("사용자 {}가 채팅방 {}에서 퇴장", userId, partyRecruitId);
    }

    public Set<Object> getRoomUsers(long partyRecruitId) {
        return redisTemplate.opsForSet().members("CHAT_ROOM_USERS:" + partyRecruitId);
    }

    public void sendMessage(long partyRecruitId, ChatMessage message) {
        String channelName = "chat." + partyRecruitId;
        redisTemplate.convertAndSend(channelName, message);
        
        // 채팅 메시지 로그 저장
        saveChatMessageLog(message);
        
        log.debug("Redis로 메시지 발행: 채널={}, 메시지={}", channelName, message.getContents());
    }

    private void saveChatMessageLog(ChatMessage message) {
        try {
            jdbcClient.sql("""
                          INSERT INTO chat_message_log (room_id, sender_id, message_type, content, metadata)
                          VALUES (:roomId, :senderId, :messageType, :content, :metadata)
                          """)
                      .param("roomId", String.valueOf(message.getPartyRecruitId()))
                      .param("senderId", message.getSenderId())
                      .param("messageType", message.getType().name())
                      .param("content", message.getContents())
                      .param("metadata", convertToJsonMetadata(message))
                      .update();
            log.debug("채팅 메시지 로그 저장 완료: roomId={}, messageId={}", message.getPartyRecruitId(), message.getMessageId());
        } catch (Exception e) {
            log.error("채팅 메시지 로그 저장 실패: {}", message.getMessageId(), e);
        }
    }

    private String convertToJsonMetadata(ChatMessage message) {
        try {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("messageId", message.getMessageId());
            metadata.put("senderName", message.getSenderName());
            metadata.put("timestamp", message.getTimestamp());
            metadata.put("application", message.isApplication());
            metadata.put("isDeleted", message.isDeleted());
            if (message.getStatusType() != null) {
                metadata.put("statusType", message.getStatusType().name());
            }
            return objectMapper.writeValueAsString(metadata);
        } catch (Exception e) {
            log.error("메타데이터 JSON 변환 실패", e);
            return "{}";
        }
    }

    public long getUserCount(String roomId) {
        return redisTemplate.opsForSet().size("CHAT_ROOM_USERS:" + roomId);
    }
}