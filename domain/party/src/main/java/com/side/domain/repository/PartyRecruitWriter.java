package com.side.domain.repository;

import com.side.domain.model.PartyRecruit;

public interface PartyRecruitWriter {

    long create(PartyRecruit partyRecruit);

    int deleteRecruit(long partyRecruitId);
}
