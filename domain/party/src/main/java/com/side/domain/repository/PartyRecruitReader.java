package com.side.domain.repository;

import com.side.domain.model.PartyRecruit;

import java.util.Optional;

public interface PartyRecruitReader {

    Optional<PartyRecruit> findById(long partyRecruitId);
}
