package com.side.rest.board.controller;

import static com.side.rest.mapper.PartyRecruitMapper.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.side.rest.board.dto.request.PartyRecruitRequestDto;
import com.side.usecase.board.PartyUseCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/party")
@RequiredArgsConstructor
public class PartyRecruitController {

	private final PartyUseCase partyUseCase;

	@PostMapping
	public ResponseEntity<Void> create(@RequestBody PartyRecruitRequestDto partyRequestDto) {

		partyUseCase.create(PartyRecruitMapper.toDomain(partyRequestDto));

		return ResponseEntity.ok(null);
	}
}
