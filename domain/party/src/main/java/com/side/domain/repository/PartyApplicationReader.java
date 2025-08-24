package com.side.domain.repository;

import com.side.domain.model.PartyApplication;

import java.util.List;
import java.util.Optional;

public interface PartyApplicationReader {

    Optional<PartyApplication> findById(long id);

    Optional<PartyApplication> findByIdAndResumeId(long id, long resumeId);

    List<PartyApplication> findByPartyRecruitId(long partyRecruitId);

    List<PartyApplication> findByResumeId(long resumeId);

    boolean existsByPartyRecruitIdAndResumeId(Long partyRecruitId, Long resumeId);

    List<Long> findPartyRecruiterCreatorAndResumeCreator(Long partyRecruitId, Long resumeId);

    Optional<PartyApplication> findByRecruitIdAndResumeId(Long partyRecruitId, Long resumeId);

    Optional<PartyApplication> findByPartyApplicationId(long partyApplicationId);

    List<PartyApplication> findAll();

    List<PartyApplication> findByUserUniqueId(Long userUniqueId);

    List<PartyApplication> findResumes(long userUniqueId);

    List<Long> getOtherApplications(long partyApplicationId, long applicationUniqueId);
}
