package com.side.domain.service;

import static com.side.domain.enums.RepositoryTypeEnum.*;
import static com.side.domain.model.UserReaction.*;

import org.springframework.stereotype.Service;

import com.side.domain.Metadata;
import com.side.domain.enums.BoardStatusTypeEnum;
import com.side.domain.model.PartyRecruit;
import com.side.domain.repository.PartyRecruitRepositoryManager;

@Service
public class PartyRecruitService {

	public void create(PartyRecruit partyRecruit) {

		PartyRecruitRepositoryManager.getDefaultPartyRecruitRepository()
									 .create(partyRecruit.toBuilder()
														 .article(partyRecruit.article().toBuilder()
																			  .userReaction(initForInsert())
																			  .build())
														 .status(BoardStatusTypeEnum.Y)
														 .metadata(Metadata.builder().build())
														 .build());
	}

	public void delete(long partyId) {
		PartyRecruitRepositoryManager.getPartyRecruitRepository(JOOQ)
									 .delete(partyId);
	}
}
