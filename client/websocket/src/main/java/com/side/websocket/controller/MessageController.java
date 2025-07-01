package com.side.websocket.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    // 클라이언트가 /api/hello로 메시지를 보내면 처리
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting(String message) {
        System.out.println(message);
        return "Hello, " + message + "!";
    }
}