package com.side.domain.service;

import org.springframework.stereotype.Service;

import com.side.domain.Metadata;
import com.side.domain.model.PartyRecruit;
import com.side.domain.model.UserReaction;
import com.side.domain.repository.PartyRecruitRepositoryManager;

@Service
public class PartyRecruitService {

	public void create(PartyRecruit partyRecruit) {

		PartyRecruitRepositoryManager.getDefaultPartyRecruitRepository()
									 .create(partyRecruit.toBuilder()
														 .article(partyRecruit.article().toBuilder()
																			  .userReaction(
																				  UserReaction.initForInsert())
																			  .build())
														 .metadata(Metadata.builder().build())
														 .build());
	}
}
