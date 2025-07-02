package com.side.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 클라이언트가 접속할 엔드포인트 등록 (SockJS 지원)
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws", "/wss")
                .setAllowedOriginPatterns("*")  // 실제 배포시엔 허용 도메인 설정 권장
                .withSockJS();
    }

    // 메시지 브로커 설정
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 브로커가 클라이언트로 메시지를 전송할 때 사용할 접두사
        registry.enableSimpleBroker("/topic", "/queue");
        // 클라이언트가 서버로 메시지를 보낼 때 사용할 접두사
        registry.setApplicationDestinationPrefixes("/api");
        registry.setUserDestinationPrefix("/user"); // 사용자별 메시징 경로 설정
    }
}

