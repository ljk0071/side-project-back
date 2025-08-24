package com.side.domain.service;

import com.side.domain.Metadata;
import com.side.domain.enums.PartyApplicationStatusTypeEnum;
import com.side.domain.exception.NotExistException;
import com.side.domain.model.PartyApplication;
import com.side.domain.model.PartyRecruit;
import com.side.domain.model.Resume;
import com.side.domain.repository.PartyApplicationReader;
import com.side.domain.repository.PartyApplicationWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
                               .partyRecruit(PartyRecruit.builder()
                                                         .id(partyRecruitId)
                                                         .build())
                               .resume(Resume.builder()
                                             .id(resumeId)
                                             .build())
                               .revision(0L)
                               .status(PartyApplicationStatusTypeEnum.PENDING)
                               .metadata(Metadata.init())
                               .build();
    }

    public PartyApplication getByApplicationId(Long applicationId) {
        return partyApplicationReader.findById(applicationId)
                                     .orElseThrow(() -> new NotExistException("지원서를 찾을 수 없습니다."));
    }

    public PartyApplication getByIdAndResumeId(long applicationId, long resumeId) {
        return partyApplicationReader.findByIdAndResumeId(applicationId, resumeId)
                                     .orElseThrow(() -> new NotExistException("지원서를 찾을 수 없습니다."));
    }

    public Optional<PartyApplication> findById(Long applicationId) {
        return partyApplicationReader.findById(applicationId);
    }

    public boolean hasAppliedToParty(Long partyRecruitId, Long resumeId) {
        return partyApplicationReader.existsByPartyRecruitIdAndResumeId(partyRecruitId, resumeId);
    }

    public boolean isMyParty(Long partyRecruitId, Long resumeId) {
        return partyApplicationReader.findPartyRecruiterCreatorAndResumeCreator(partyRecruitId, resumeId)
                                     .stream()
                                     .distinct()
                                     .count() == 1;
    }

    public List<PartyApplication> findByUserUniqueId(Long userUniqueId) {
        return partyApplicationReader.findByUserUniqueId(userUniqueId);
    }

    public void changeStatus(long partyApplicationId, PartyApplicationStatusTypeEnum status) {

        partyApplicationWriter.changeStatus(partyApplicationId, status);
    }

    public List<PartyApplication> findResumes(long userUniqueId) {
        return partyApplicationReader.findResumes(userUniqueId);
    }

    public List<Long> getOtherApplications(long partyApplicationId, long applicationUniqueId) {
        return partyApplicationReader.getOtherApplications(partyApplicationId, applicationUniqueId);
    }
}