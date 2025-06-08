//package com.side.websocket.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.event.EventListener;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class CustomEventListener {
//
//    private final SimpMessagingTemplate messagingTemplate;
//
//    // 예시 커스텀 이벤트 처리기 (예: 특정 비즈니스 이벤트 발생)
//    @EventListener
//    public void handleCustomEvent() {
//
//        messagingTemplate.convertAndSend("/queue/updates", "");
//    }
//}