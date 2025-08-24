package com.side.usecase.resume;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.domain.KoreanJosaUtil;
import com.side.domain.enums.PartyApplicationStatusTypeEnum;
import com.side.domain.exception.DuplicatePartyApplicationException;
import com.side.domain.exception.NotExistException;
import com.side.domain.model.PartyApplication;
import com.side.domain.service.PartyApplicationService;
import com.side.domain.service.PartyRecruitService;
import com.side.websocket.model.ChatMessage;
import com.side.websocket.service.ChatRoomService;
import com.side.websocket.service.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.side.websocket.model.ChatMessage.notifyApplication;

@RequiredArgsConstructor
@Service
public class PartyApplicationUseCase {

    private static final String IS_EXIST_PARTY_RECRUIT = "isExistPartyRecruit";
    private static final String HAS_APPLIED_TO_PARTY = "hasAppliedToParty";
    private static final String IS_MY_PARTY = "isMyParty";

    private final PartyRecruitService partyRecruitService;
    private final PartyApplicationService partyApplicationService;
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisSubscriber redisSubscriber;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatRoomService chatRoomService;
    private final ObjectMapper objectMapper;
    private final Executor executor;

    private final Map<Long, ChannelTopic> channelTopics = new ConcurrentHashMap<>();

    /**
     * 주어진 partyRecruitId와 resumeId에 대한 새로운 파티 신청을 생성합니다.
     *
     * @param partyRecruitId 파티 모집글의 ID
     * @param resumeId       신청에 연결된 이력서의 ID
     * @return 새로운 신청 ID를 나타내는 생성된 키 값
     */
    @Transactional
    public long create(long partyRecruitId, long resumeId) {

        validationForCreate(partyRecruitId, resumeId);

        long partyApplicationId = partyApplicationService.create(partyRecruitId, resumeId);

        PartyApplication application = partyApplicationService.getByIdAndResumeId(partyApplicationId, resumeId);

        try {
            chatRoomService.sendMessage(partyRecruitId, notifyApplication(partyRecruitId, objectMapper.writeValueAsString(Map.of("application", application))));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        ChannelTopic topic = channelTopics.computeIfAbsent(partyRecruitId, k -> new ChannelTopic("notify:" + application.resume()
                                                                                                                        .userUniqueId()));
        redisMessageListenerContainer.addMessageListener(redisSubscriber, topic);

        return partyApplicationId;
    }

    public void validationForCreate(long partyRecruitId, long resumeId) {

        Map<String, Boolean> validationResult = new ConcurrentHashMap<>();

        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {

            executorService.execute(() -> validationResult.put(IS_EXIST_PARTY_RECRUIT, partyRecruitService.isExistPartyRecruit(partyRecruitId)));

            executorService.execute(() -> validationResult.put(IS_MY_PARTY, partyApplicationService.isMyParty(partyRecruitId, resumeId)));

            executorService.execute(() -> validationResult.put(HAS_APPLIED_TO_PARTY, partyApplicationService.hasAppliedToParty(partyRecruitId, resumeId)));
        }

        if (!validationResult.get(IS_EXIST_PARTY_RECRUIT)) {
            throw new NotExistException("존재하지 않는 파티모집글 입니다.", partyRecruitId);
        }

        if (validationResult.get(IS_MY_PARTY)) {
            throw new NotExistException("자신의 파티모집글에는 지원 할 수 없습니다.", partyRecruitId);
        }

        if (validationResult.get(HAS_APPLIED_TO_PARTY)) {
            throw new DuplicatePartyApplicationException(
                    "이미 해당 파티에 지원하셨습니다.",
                    partyRecruitId,
                    partyRecruitService.getByRecruitId(partyRecruitId)
                                       .article()
                                       .contents()
            );
        }
    }

    /**
     * 현재 사용자의 파티 지원 목록을 조회합니다.
     *
     * @param userUniqueId 사용자 고유 ID
     * @return 사용자가 지원한 파티 신청 목록
     */
    @Transactional(readOnly = true)
    public List<PartyApplication> findByUserUniqueId(Long userUniqueId) {
        return partyApplicationService.findByUserUniqueId(userUniqueId);
    }

    @Transactional
    public String changeStatus(long partyApplicationId, PartyApplicationStatusTypeEnum status) {

        partyApplicationService.changeStatus(partyApplicationId, status);

        if (status == PartyApplicationStatusTypeEnum.ACCEPTED) {

            PartyApplication partyApplication = partyApplicationService.getByApplicationId(partyApplicationId);

            val partyRecruitId = partyApplication.partyRecruit().id();

            val applicantUniqueId = partyApplication.resume().userUniqueId();

            List<Long> otherIds = partyApplicationService.getOtherApplications(partyApplicationId, applicantUniqueId);

            otherIds.forEach(otherId -> partyApplicationService.changeStatus(otherId, PartyApplicationStatusTypeEnum.CANCELED));

            redisTemplate.convertAndSend("notify:" + applicantUniqueId, ChatMessage.notifyToSubscribe(partyRecruitId, applicantUniqueId, PartyApplicationStatusTypeEnum.ACCEPTED));
        }

        if (status == PartyApplicationStatusTypeEnum.REJECTED) {
            PartyApplication partyApplication = partyApplicationService.getByApplicationId(partyApplicationId);

            val partyRecruitId = partyApplication.partyRecruit().id();

            val applicantUniqueId = partyApplication.resume().userUniqueId();

            redisTemplate.convertAndSend("notify:" + applicantUniqueId, ChatMessage.notifyToSubscribe(partyRecruitId, applicantUniqueId, PartyApplicationStatusTypeEnum.REJECTED));
        }

        return KoreanJosaUtil.JosaBuilder.of(status.getNote()).euro() + " 변경 되었습니다.";
    }

    public List<PartyApplication> findResumes(long userUniqueId) {
        return partyApplicationService.findResumes(userUniqueId);
    }
}