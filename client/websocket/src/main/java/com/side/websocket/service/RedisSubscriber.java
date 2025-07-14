package com.side.websocket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.websocket.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String channelName = new String(message.getChannel());
            String publishMessage = new String(message.getBody());

            log.debug("Redis 메시지 수신 - 채널: {}, 내용: {}", channelName, publishMessage);

            ChatMessage chatMessage = objectMapper.readValue(publishMessage, ChatMessage.class);
            String destination = "/topic/chat/" + chatMessage.getRoomId();

            messagingTemplate.convertAndSend(destination, chatMessage);

            log.debug("WebSocket 브로드캐스트 완료 - 목적지: {}, 발신자: {}, 내용: {}",
                    destination, chatMessage.getSenderName(), chatMessage.getContent());
        } catch (JsonProcessingException e) {
            log.error("Redis 메시지 파싱 실패: {}", new String(message.getBody()), e);
        }
    }
}