package com.side.domain.service;

import com.side.domain.Metadata;
import com.side.domain.enums.PartyApplicationStatusTypeEnum;
import com.side.domain.model.PartyApplication;
import com.side.domain.repository.PartyApplicationReader;
import com.side.domain.repository.PartyApplicationWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PartyApplicationService {

    private final PartyApplicationReader partyApplicationReader;

    private final PartyApplicationWriter partyApplicationWriter;

    public long create(Long partyRecruitId, Long resumeId) {

        return partyApplicationWriter.create(initForCreate(partyRecruitId, resumeId));
    }

    public PartyApplication initForCreate(Long partyRecruitId, Long resumeId) {

        return PartyApplication.builder()
                               .partyRecruitId(partyRecruitId)
                               .resumeId(resumeId)
                               .revision(0L)
                               .status(PartyApplicationStatusTypeEnum.PENDING)
                               .metadata(Metadata.init())
                               .build();
    }

    public PartyApplication getByApplicationId(Long applicationId) {
        return partyApplicationReader.findById(applicationId)
                                     .orElseThrow(() -> new IllegalArgumentException("지원서를 찾을 수 없습니다."));
    }

    public Optional<PartyApplication> findById(Long applicationId) {
        return partyApplicationReader.findById(applicationId);
    }

    public boolean hasAppliedToParty(Long partyRecruitId, Long resumeId) {
        return partyApplicationReader.existsByPartyRecruitIdAndResumeId(partyRecruitId, resumeId);
    }
}