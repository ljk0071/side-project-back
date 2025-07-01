package com.side.domain.service;

import com.side.domain.Metadata;
import com.side.domain.StatusTypeEnum;
import com.side.domain.model.PartyRecruit;
import com.side.domain.repository.PartyRecruitRepositoryManager;
import org.springframework.stereotype.Service;

import static com.side.domain.RepositoryTypeEnum.JOOQ;

@Service
public class PartyRecruitService {

    public void create(PartyRecruit partyRecruit) {

        PartyRecruitRepositoryManager.getDefaultPartyRecruitRepository()
                                     .create(partyRecruit.toBuilder()
                                                         .revision(0L)
                                                         .status(StatusTypeEnum.Y)
                                                         .metadata(Metadata.builder().build())
                                                         .build());
    }

    public void delete(long partyId) {
        PartyRecruitRepositoryManager.getPartyRecruitRepository(JOOQ)
                                     .delete(partyId);
    }
}
