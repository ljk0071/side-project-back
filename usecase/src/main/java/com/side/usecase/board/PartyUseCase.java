package com.side.usecase.board;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.side.domain.model.PartyRecruit;
import com.side.domain.service.PartyRecruitService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PartyUseCase {

	private final PartyRecruitService partyRecruitService;

	@Transactional
	public void create(PartyRecruit partyRecruit) {
		partyRecruitService.create(partyRecruit);
	}
}
