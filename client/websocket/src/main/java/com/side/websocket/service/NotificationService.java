package com.side.websocket.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 특정 사용자에게 개인 메시지 전송
     * 
     * @param userUniqueId 수신자 고유 ID
     * @param message 전송할 메시지
     */
    public void sendToUser(Long userUniqueId, Object message) {
        String destination = "/queue/notifications";
        messagingTemplate.convertAndSendToUser(String.valueOf(userUniqueId), destination, message);
        log.info("개인 메시지 전송 - 사용자: {}, 목적지: {}", userUniqueId, destination);
    }

    /**
     * 특정 사용자에게 파티 관련 알림 전송
     * 
     * @param userUniqueId 수신자 고유 ID
     * @param partyRecruitId 파티 모집글 ID
     * @param message 전송할 메시지
     */
    public void sendPartyNotificationToUser(Long userUniqueId, Long partyRecruitId, Object message) {
        String destination = "/queue/party/" + partyRecruitId;
        messagingTemplate.convertAndSendToUser(String.valueOf(userUniqueId), destination, message);
        log.info("파티 알림 전송 - 사용자: {}, 파티: {}, 목적지: {}", userUniqueId, partyRecruitId, destination);
    }

    /**
     * 전체 사용자에게 공지사항 전송
     * 
     * @param message 전송할 메시지
     */
    public void sendToAll(Object message) {
        String destination = "/topic/announcements";
        messagingTemplate.convertAndSend(destination, message);
        log.info("전체 공지 전송 - 목적지: {}", destination);
    }

    /**
     * 특정 파티의 모든 참여자에게 메시지 전송
     * 
     * @param partyRecruitId 파티 모집글 ID
     * @param message 전송할 메시지
     */
    public void sendToParty(Long partyRecruitId, Object message) {
        String destination = "/topic/party/" + partyRecruitId;
        messagingTemplate.convertAndSend(destination, message);
        log.info("파티 메시지 전송 - 파티: {}, 목적지: {}", partyRecruitId, destination);
    }
}