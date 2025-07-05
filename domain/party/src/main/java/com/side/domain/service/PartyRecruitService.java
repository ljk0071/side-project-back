package com.side.domain.service;

import com.side.domain.Metadata;
import com.side.domain.YesNoDeleteStatus;
import com.side.domain.exception.NotExistException;
import com.side.domain.model.PartyRecruit;
import com.side.domain.repository.PartyRecruitReader;
import com.side.domain.repository.PartyRecruitWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public Optional<PartyRecruit> findById(long partyRecruitId) {
        return partyRecruitReader.findById(partyRecruitId);
    }

    public PartyRecruit getById(long partyRecruitId) {
        return partyRecruitReader.findById(partyRecruitId)
                                 .orElseThrow(() -> new NotExistException("존재하지 않는 파티모집글 입니다.", partyRecruitId));
    }

    public boolean isExistPartyRecruit(long partyRecruitId) {
        return findById(partyRecruitId).isPresent();
    }
}
