package com.side.websocket.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.bootstrap.SideApplication;
import com.side.websocket.dto.ChatRoomDto;
import com.side.websocket.service.ChatRoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = SideApplication.class)
@DisplayName("채팅 통합 테스트")
class ChatIntegrationTest {

    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private ObjectMapper objectMapper;
    private WebSocketStompClient stompClient;
    private String roomId;
    private WebSocketHttpHeaders headers;

    @BeforeEach
    void setUp() throws Exception {
        stompClient = new WebSocketStompClient(new SockJsClient(List.of(new WebSocketTransport(new StandardWebSocketClient()))));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter(objectMapper));

        // 테스트용 채팅방 생성
        roomId = createTestChatRoom();

        headers = new WebSocketHttpHeaders();
        headers.add("Authorization", "Bearer eyJ0eXAiOiJqd3QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiQUNDRVNTIiwic3ViIjoic3lzdGVtIiwiaWF0IjoxNzUxNjE1OTI4LCJleHAiOjE3Nzc1MzU5MjgsInJvbGVzIjpbIlJPTEVfQURNSU4iXX0.bTEcKisQBAiK5mMm3tSUBiT2gQnXx0RjAFCLt6OZfR8");
        headers.add("X-Csrf-Token", "eyJ0eXAiOiJqd3QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiQ1NSRiIsInN1YiI6InN5c3RlbSIsImlhdCI6MTc1MTYxNTkyOCwiZXhwIjoxNzc3NTM1OTI4fQ.3w9Eqbpljqac0G1ClVPoJNtDV67HSCzB8Q_y3o8Vy1w");
    }

    private String createTestChatRoom() throws Exception {
        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer eyJ0eXAiOiJqd3QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiQUNDRVNTIiwic3ViIjoic3lzdGVtIiwiaWF0IjoxNzUxNjE1OTI4LCJleHAiOjE3Nzc1MzU5MjgsInJvbGVzIjpbIlJPTEVfQURNSU4iXX0.bTEcKisQBAiK5mMm3tSUBiT2gQnXx0RjAFCLt6OZfR8");
        headers.add("X-Csrf-Token", "eyJ0eXAiOiJqd3QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiQ1NSRiIsInN1YiI6InN5c3RlbSIsImlhdCI6MTc1MTYxNTkyOCwiZXhwIjoxNzc3NTM1OTI4fQ.3w9Eqbpljqac0G1ClVPoJNtDV67HSCzB8Q_y3o8Vy1w");


        // 요청 바디 생성
        ChatRoomDto.CreateRequest createRequest = new ChatRoomDto.CreateRequest();
        createRequest.setRoomName("테스트 채팅방");

        HttpEntity<ChatRoomDto.CreateRequest> requestEntity = new HttpEntity<>(createRequest, headers);

        // REST API 호출로 채팅방 생성
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ChatRoomDto> response = restTemplate.postForEntity(
                "http://localhost:8080/api/chat/rooms",
                requestEntity,
                ChatRoomDto.class
        );

        if (response.getStatusCode() == HttpStatus.CREATED && response.getBody() != null) {
            String createdRoomId = response.getBody().getRoomId();
            System.out.println("테스트용 채팅방 생성됨: " + createdRoomId);
            return createdRoomId;
        } else {
            throw new RuntimeException("채팅방 생성 실패: " + response.getStatusCode());
        }
    }

    @Test
    @DisplayName("WebSocket 연결 테스트")
    void webSocketConnectionTest() throws Exception {

        // Given
        String url = "ws://localhost:8080/ws/chat";

        // When
        StompSession session = stompClient.connectAsync(url, headers, new TestStompSessionHandler())
                                          .get(5, TimeUnit.SECONDS);

        // Then
        assertThat(session.getSessionId()).isNotBlank();

        session.disconnect();
    }

    @Test
    @DisplayName("WebSocket 구독 테스트")
    void webSocketSubscriptionTest() throws Exception {
        // Given
        String url = "ws://localhost:8080/ws/chat";

        StompSession session = stompClient.connectAsync(url, headers, new TestStompSessionHandler())
                                          .get(5, TimeUnit.SECONDS);

        // When
        StompSession.Subscription subscription = session.subscribe("/topic/chat/" + roomId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                messageQueue.offer((String) payload);
            }
        });

        session.send("/chat/" + roomId, "test");

        // Then
        assertThat(subscription).isNotNull();

        session.disconnect();
    }

    // 테스트용 StompSessionHandler 구현
    private static class TestStompSessionHandler extends StompSessionHandlerAdapter {
        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            System.out.println("WebSocket 연결 성공: " + session.getSessionId());
        }

        @Override
        public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
            System.err.println("WebSocket 에러: " + exception.getMessage());
        }
    }
}