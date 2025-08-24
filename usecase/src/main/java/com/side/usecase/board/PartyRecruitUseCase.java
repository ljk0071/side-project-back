package com.side.usecase.board;

import com.side.domain.Search;
import com.side.domain.exception.NotMyPartyRecruit;
import com.side.domain.model.PartyRecruit;
import com.side.domain.service.PartyRecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class PartyRecruitUseCase {

    private final PartyRecruitService partyRecruitService;
    private static final String IS_MY_PARTY_RECRUIT = "isMyPartyRecruit";

    @Transactional
    public long create(PartyRecruit partyRecruit) {
        return partyRecruitService.create(partyRecruit);
    }

    public List<PartyRecruit> getActiveRecruits(Search search) {

        return partyRecruitService.getActiveRecruits(search);
    }

    public Optional<PartyRecruit> findByUserUniqueId(long userUniqueId) {
        return partyRecruitService.findByUserUniqueId(userUniqueId);
    }

    public PartyRecruit findByRecruitId(long partyRecruitId) {

        return partyRecruitService.getByRecruitId(partyRecruitId);
    }

    @Transactional
    public void deleteRecruit(long partyRecruitId, long userUniqueId) {

        validationForDelete(partyRecruitId, userUniqueId);

        partyRecruitService.deleteRecruit(partyRecruitId);
    }

    public void validationForDelete(long partyRecruitId, long userUniqueId) {
        Map<String, Boolean> validationMap = new HashMap<>();
        try (var executors = Executors.newVirtualThreadPerTaskExecutor()) {
            executors.execute(() -> validationMap.put(IS_MY_PARTY_RECRUIT, partyRecruitService.isMyPartyRecruit(partyRecruitId, userUniqueId)));
        }

        if (!validationMap.get(IS_MY_PARTY_RECRUIT)) {
            throw new NotMyPartyRecruit("존재하지 않는 파티모집글 입니다.", partyRecruitId, userUniqueId);
        }
    }
}
