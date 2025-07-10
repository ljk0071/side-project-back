package com.side.domain.repository;

import com.side.domain.model.PartyApplication;

import java.util.List;
import java.util.Optional;

public interface PartyApplicationReader {

    Optional<PartyApplication> findById(long id);

    List<PartyApplication> findByPartyRecruitId(long partyRecruitId);

    List<PartyApplication> findByResumeId(long resumeId);

    boolean existsByPartyRecruitIdAndResumeId(Long partyRecruitId, Long resumeId);

    Optional<PartyApplication> findByRecruitIdAndResumeId(Long partyRecruitId, Long resumeId);

    Optional<PartyApplication> findByPartyApplicationId(long partyApplicationId);

    List<PartyApplication> findAll();
}
