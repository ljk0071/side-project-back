package com.side.websocket.controller;

import com.side.domain.model.User;
import com.side.security.service.SecurityHelper;
import com.side.websocket.dto.ChatMessageDto;
import com.side.websocket.dto.ChatRoomDto;
import com.side.websocket.model.ChatMessage;
import com.side.websocket.model.ChatRoom;
import com.side.websocket.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatRoomService chatRoomService;

    @MessageMapping("/chat/{roomId}/send")
    public void sendMessage(
            @DestinationVariable(value = "roomId") String roomId,
            @Payload ChatMessageDto messageDto,
            Principal principal
    ) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Long userUniqueId = user.uniqueId();
        String userName = user.name();

        if (userUniqueId == null || userName == null) {
            log.error("WebSocket 세션에 인증 정보가 없습니다. 메시지 전송 실패");
            return;
        }

        ChatMessage message = ChatMessage.builder()
                                         .messageId(generateMessageId())
                                         .roomId(roomId)
                                         .senderId(userUniqueId)
                                         .senderName(userName)
                                         .content(messageDto.getContent())
                                         .type(ChatMessage.MessageType.CHAT)
                                         .build();

        chatRoomService.sendMessage(roomId, message);
    }

    @MessageMapping("/chat/{roomId}/join")
    public void joinChatRoom(
            @DestinationVariable(value = "roomId") String roomId,
            Principal principal
    ) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Long userUniqueId = user.uniqueId();
        String userName = user.name();

        if (userUniqueId == null || userName == null) {
            log.error("WebSocket 세션에 인증 정보가 없습니다. 채팅방 입장 실패");
            return;
        }

        ChatRoom room = chatRoomService.findRoomById(roomId);
        if (room == null) {
            log.warn("존재하지 않는 채팅방 입장 시도: {}", roomId);
            return;
        }

        if (room.isFull()) {
            log.warn("채팅방 정원 초과: {}", roomId);
            return;
        }

        chatRoomService.enterChatRoom(roomId, userUniqueId);

        ChatMessage joinMessage = ChatMessage.createJoinMessage(roomId, userUniqueId, userName);
        chatRoomService.sendMessage(roomId, joinMessage);
    }

    @MessageMapping("/chat/{roomId}/leave")
    public void leaveChatRoom(
            @DestinationVariable(value = "roomId") String roomId,
            Principal principal
    ) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Long userId = user.uniqueId();
        String userName = user.name();

        chatRoomService.leaveChatRoom(roomId, userId);

        ChatMessage leaveMessage = ChatMessage.createLeaveMessage(roomId, userId, userName);
        chatRoomService.sendMessage(roomId, leaveMessage);

        chatRoomService.sendMessage(roomId, ChatMessage.builder()
                                                       .roomId(roomId)
                                                       .content(String.valueOf(chatRoomService.getRoomUsers(roomId)))
                                                       .build());
    }

    @PostMapping("/api/chat/rooms")
    @ResponseBody
    public ResponseEntity<ChatRoomDto> createChatRoom(@RequestBody ChatRoomDto.CreateRequest request) {
        Long creatorId = SecurityHelper.getAuthenticatedUserUniqueId();

        ChatRoom room = chatRoomService.createChatRoom(request.getRoomName(), creatorId);

        ChatRoomDto responseDto = ChatRoomDto.builder()
                                             .roomId(room.getRoomId())
                                             .roomName(room.getRoomName())
                                             .creatorId(room.getCreatorId())
                                             .participantCount(room.getParticipantCount())
                                             .maxParticipants(room.getMaxParticipants())
                                             .isActive(room.isActive())
                                             .createdAt(room.getCreatedAt())
                                             .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/api/chat/rooms")
    @ResponseBody
    public List<ChatRoomDto> getAllChatRooms() {
        return chatRoomService.findAllRooms()
                              .stream()
                              .map(room -> ChatRoomDto.builder()
                                                      .roomId(room.getRoomId())
                                                      .roomName(room.getRoomName())
                                                      .creatorId(room.getCreatorId())
                                                      .participantCount(room.getParticipantCount())
                                                      .maxParticipants(room.getMaxParticipants())
                                                      .isActive(room.isActive())
                                                      .createdAt(room.getCreatedAt())
                                                      .build())
                              .collect(Collectors.toList());
    }

    @GetMapping("/api/chat/rooms/{roomId}")
    @ResponseBody
    public ChatRoomDto getChatRoom(@PathVariable String roomId) {
        ChatRoom room = chatRoomService.findRoomById(roomId);
        if (room == null) {
            throw new RuntimeException("채팅방을 찾을 수 없습니다: " + roomId);
        }

        return ChatRoomDto.builder()
                          .roomId(room.getRoomId())
                          .roomName(room.getRoomName())
                          .creatorId(room.getCreatorId())
                          .participantCount(room.getParticipantCount())
                          .maxParticipants(room.getMaxParticipants())
                          .isActive(room.isActive())
                          .createdAt(room.getCreatedAt())
                          .build();
    }

    @GetMapping("/api/chat/rooms/{roomId}/users")
    @ResponseBody
    public Set<Object> getChatRoomUsers(@PathVariable(value = "roomId") String roomId) {
        return chatRoomService.getRoomUsers(roomId);
    }

    private String generateMessageId() {
        return "MSG_" + System.currentTimeMillis() + "_" + System.nanoTime();
    }
}