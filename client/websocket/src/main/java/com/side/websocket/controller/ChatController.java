package com.side.websocket.controller;

import com.side.domain.model.User;
import com.side.security.service.SecurityHelper;
import com.side.websocket.dto.ChatMessageDto;
import com.side.websocket.dto.ChatRoomResponseDto;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;
import java.util.Set;

import static com.side.websocket.mapper.ChatRoomMapper.ChatRoomMapper;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatRoomService chatRoomService;

    @MessageMapping("/chat/{partyRecruitId}/send")
    public void sendMessage(
            @DestinationVariable(value = "partyRecruitId") long partyRecruitId,
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
                                         .partyRecruitId(partyRecruitId)
                                         .senderId(userUniqueId)
                                         .senderName(userName)
                                         .contents(messageDto.getContents())
                                         .type(ChatMessage.MessageType.CHAT)
                                         .build();

        chatRoomService.sendMessage(partyRecruitId, message);
    }

    @MessageMapping("/chat/{partyRecruitId}/join")
    public void joinChatRoom(
            @DestinationVariable(value = "partyRecruitId") long partyRecruitId,
            Principal principal
    ) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Long userUniqueId = user.uniqueId();
        String userName = user.name();

        if (userUniqueId == null || userName == null) {
            log.error("WebSocket 세션에 인증 정보가 없습니다. 채팅방 입장 실패");
            return;
        }

        boolean isAlreadyJoined = chatRoomService.isAlreadyJoined(partyRecruitId, userUniqueId);

        chatRoomService.enterChatRoom(partyRecruitId, userUniqueId);

        if (!isAlreadyJoined) {
            ChatMessage joinMessage = ChatMessage.createJoinMessage(partyRecruitId, userName);
            chatRoomService.sendMessage(partyRecruitId, joinMessage);
        }
    }

    @MessageMapping("/chat/{partyRecruitId}/leave")
    public void leaveChatRoom(
            @DestinationVariable(value = "partyRecruitId") long partyRecruitId,
            Principal principal
    ) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Long userId = user.uniqueId();
        String userName = user.name();

        chatRoomService.leaveChatRoom(partyRecruitId, userId);

        ChatMessage leaveMessage = ChatMessage.createLeaveMessage(partyRecruitId, userId, userName);
        chatRoomService.sendMessage(partyRecruitId, leaveMessage);

        chatRoomService.sendMessage(partyRecruitId, ChatMessage.builder()
                                                               .partyRecruitId(partyRecruitId)
                                                               .contents(String.valueOf(chatRoomService.getRoomUsers(partyRecruitId)))
                                                               .build());
    }

    @PostMapping("/api/chat/rooms/{partyRecruitId}")
    @ResponseBody
    public ResponseEntity<ChatRoomResponseDto> createChatRoom(
            @PathVariable(name = "partyRecruitId") long partyRecruitId
    ) {

        Long creatorId = SecurityHelper.getAuthenticatedUserUniqueId();

        ChatRoom room = chatRoomService.createChatRoom(partyRecruitId, creatorId);

        ChatRoomResponseDto responseDto = ChatRoomMapper.toResponse(room);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/api/chat/rooms")
    @ResponseBody
    public List<ChatRoomResponseDto> getAllChatRooms() {
        return ChatRoomMapper.toResponseList(chatRoomService.findAllRooms());
    }

    @GetMapping("/api/chat/rooms/{partyRecruitId}")
    @ResponseBody
    public ChatRoomResponseDto getChatRoom(@PathVariable String partyRecruitId) {
        ChatRoom room = chatRoomService.findRoomById(partyRecruitId);
        if (room == null) {
            throw new RuntimeException("채팅방을 찾을 수 없습니다: " + partyRecruitId);
        }

        return ChatRoomMapper.toResponse(room);
    }

    @GetMapping("/api/chat/rooms/{partyRecruitId}/users")
    @ResponseBody
    public Set<Object> getChatRoomUsers(@PathVariable(name = "partyRecruitId") long partyRecruitId) {
        return chatRoomService.getRoomUsers(partyRecruitId);
    }

    private String generateMessageId() {
        return "MSG_" + System.currentTimeMillis() + "_" + System.nanoTime();
    }
}