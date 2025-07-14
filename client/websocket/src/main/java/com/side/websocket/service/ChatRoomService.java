package com.side.websocket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.websocket.model.ChatMessage;
import com.side.websocket.model.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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

    private final Map<String, ChannelTopic> channelTopics = new ConcurrentHashMap<>();

    public ChatRoom createChatRoom(String roomName, Long creatorId) {
        String roomId = generateRoomId();
        ChatRoom chatRoom = ChatRoom.builder()
                                    .roomId(roomId)
                                    .roomName(roomName)
                                    .creatorId(creatorId)
                                    .build();

        redisTemplate.opsForHash().put("CHAT_ROOM", roomId, chatRoom);

        log.info("채팅방 생성: {}", roomId);
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

    public void enterChatRoom(String roomId, Long userId) {
        String channelName = "chat." + roomId;
        ChannelTopic topic = channelTopics.computeIfAbsent(roomId, k -> new ChannelTopic(channelName));

        redisMessageListenerContainer.addMessageListener(redisSubscriber, topic);
        redisTemplate.opsForSet().add("CHAT_ROOM_USERS:" + roomId, userId);

        log.info("사용자 {}가 채팅방 {}에 입장", userId, roomId);
    }

    public void leaveChatRoom(String roomId, Long userId) {
        redisTemplate.opsForSet().remove("CHAT_ROOM_USERS:" + roomId, userId);

        if (redisTemplate.opsForSet().size("CHAT_ROOM_USERS:" + roomId) == 0) {
            ChannelTopic topic = channelTopics.remove(roomId);
            if (topic != null) {
                redisMessageListenerContainer.removeMessageListener(redisSubscriber, topic);
            }
        }

        log.info("사용자 {}가 채팅방 {}에서 퇴장", userId, roomId);
    }

    public Set<Object> getRoomUsers(String roomId) {
        return redisTemplate.opsForSet().members("CHAT_ROOM_USERS:" + roomId);
    }

    public void sendMessage(String roomId, ChatMessage message) {
        String channelName = "chat." + roomId;
        redisTemplate.convertAndSend(channelName, message);
        log.debug("Redis로 메시지 발행: 채널={}, 메시지={}", channelName, message.getContent());
    }

    public long getUserCount(String roomId) {
        return redisTemplate.opsForSet().size("CHAT_ROOM_USERS:" + roomId);
    }

    private String generateRoomId() {
        return "ROOM_" + System.currentTimeMillis();
    }
}