package com.side.domain.repository;

import com.side.domain.Search;
import com.side.domain.model.PartyRecruit;

import java.util.List;
import java.util.Optional;

public interface PartyRecruitReader {

    Optional<PartyRecruit> findByRecruitId(long partyRecruitId);

    List<PartyRecruit> getActiveRecruits(Search search);
}
