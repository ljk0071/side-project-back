package com.side.usecase.resume;

import com.side.domain.exception.DuplicatePartyApplicationException;
import com.side.domain.exception.NotExistException;
import com.side.domain.service.PartyApplicationService;
import com.side.domain.service.PartyRecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class PartyApplicationUseCase {

    private final PartyRecruitService partyRecruitService;
    private final PartyApplicationService partyApplicationService;

    private static final String IS_EXIST_PARTY_RECRUIT = "isExistPartyRecruit";

    private static final String HAS_APPLIED_TO_PARTY = "hasAppliedToParty";

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

        return partyApplicationService.create(partyRecruitId, resumeId);
    }

    public void validationForCreate(long partyRecruitId, long resumeId) {

        Map<String, Boolean> validationResult = new ConcurrentHashMap<>();

        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {

            executorService.execute(() -> validationResult.put(IS_EXIST_PARTY_RECRUIT, partyRecruitService.isExistPartyRecruit(partyRecruitId)));

            executorService.execute(() -> validationResult.put(HAS_APPLIED_TO_PARTY, partyApplicationService.hasAppliedToParty(partyRecruitId, resumeId)));
        }

        if (!validationResult.get(IS_EXIST_PARTY_RECRUIT)) {
            throw new NotExistException("존재하지 않는 파티모집글 입니다.", partyRecruitId);
        }

        if (validationResult.get(HAS_APPLIED_TO_PARTY)) {
            throw new DuplicatePartyApplicationException(
                    "이미 해당 파티에 지원하셨습니다.",
                    partyRecruitId,
                    partyRecruitService.getById(partyRecruitId)
                                       .article()
                                       .title()
            );
        }
    }
}