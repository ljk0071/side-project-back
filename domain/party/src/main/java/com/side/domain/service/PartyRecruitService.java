package com.side.domain.service;

import com.side.domain.Metadata;
import com.side.domain.Search;
import com.side.domain.YesNoDeleteStatus;
import com.side.domain.exception.InvalidSearchCondition;
import com.side.domain.exception.NotExistException;
import com.side.domain.model.PartyRecruit;
import com.side.domain.repository.PartyRecruitReader;
import com.side.domain.repository.PartyRecruitWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PartyRecruitService {

    private final PartyRecruitReader partyRecruitReader;

    private final PartyRecruitWriter partyRecruitWriter;

    public long create(PartyRecruit partyRecruit) {

        return partyRecruitWriter.create(initForCreate(partyRecruit));
    }

    public PartyRecruit initForCreate(PartyRecruit partyRecruit) {

        return partyRecruit.toBuilder()
                           .revision(0L)
                           .status(YesNoDeleteStatus.YES)
                           // jpa autoAudit을 위해 null이면 안됨
                           .metadata(Metadata.init())
                           .build();
    }

    public Optional<PartyRecruit> findByRecruitId(long partyRecruitId) {
        return partyRecruitReader.findByRecruitId(partyRecruitId);
    }

    public PartyRecruit getByRecruitId(long partyRecruitId) {
        return partyRecruitReader.findByRecruitId(partyRecruitId)
                                 .orElseThrow(() -> new NotExistException("존재하지 않는 파티모집글 입니다.", partyRecruitId));
    }

    public boolean isMyPartyRecruit(long partyRecruitId, long userUniqueId) {
        PartyRecruit partyRecruit = getByRecruitId(partyRecruitId);
        return userUniqueId == partyRecruit.userUniqueId();
    }

    public int deleteRecruit(long partyRecruitId) {

        return partyRecruitWriter.deleteRecruit(partyRecruitId);
    }

    public boolean isExistPartyRecruit(long partyRecruitId) {
        return findByRecruitId(partyRecruitId).isPresent();
    }

    public List<PartyRecruit> getActiveRecruits(Search search) {

        validateSearch(search);

        return partyRecruitReader.getActiveRecruits(search);
    }

    private void validateSearch(Search search) {
        if (CollectionUtils.isEmpty(search.searchConditions())) {
            throw new InvalidSearchCondition("검색조건이 존재하지 않습니다. 검색 조건은 하나 이상이어야 합니다.");
        }
    }
}
